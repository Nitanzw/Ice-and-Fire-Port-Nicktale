"""Diagnostic-driven line fixer. Reads build/compile.log and rewrites only the lines javac flagged.
Usage: python tools/autofix.py [--dry]
"""
import collections
import os
import re
import sys

dry = '--dry' in sys.argv
LOG = 'build/compile.log'


def parse():
    log = open(LOG, encoding='utf-8-sig', errors='replace').read().split('\n')
    items = []
    i = 0
    while i < len(log):
        m = re.match(r'(.+?\.java):(\d+): error: (.*)', log[i])
        if m:
            j = i + 1
            block = []
            while j < len(log) and not re.match(r'.+?\.java:\d+: error:', log[j]) and j < i + 9:
                block.append(log[j])
                j += 1
            items.append((m.group(1), int(m.group(2)), m.group(3), '\n'.join(block)))
            i = j
        else:
            i += 1
    return items


def r_sound(line, msg, block):
    if 'Reference<SoundEvent> cannot be converted to SoundEvent' not in msg:
        return None
    return re.sub(r'(SoundEvents\.[A-Z][A-Z0-9_]*)(?!\.value\(\)|\w)', lambda m: m.group(1) + '.value()', line)


def r_spawn(line, msg, block):
    if 'spawnAtLocation' not in msg + block:
        return None
    new = re.sub(r'(?<![\w.])this\.spawnAtLocation\(', 'IafEntityUtil.drop(this, ', line)
    new = re.sub(r'(?<![\w.])spawnAtLocation\(', 'IafEntityUtil.drop(this, ', new)
    new = re.sub(r'([\w.()]+?)\.spawnAtLocation\(', lambda m: 'IafEntityUtil.drop(%s, ' % m.group(1), new)
    return new if new != line else None


def r_sided(line, msg, block):
    if 'sidedSuccess' not in block:
        return None
    return re.sub(r'InteractionResult\.sidedSuccess\([^;]*?\)(?=;)', 'InteractionResult.SUCCESS', line)


def r_step(line, msg, block):
    if 'setMaxUpStep' not in block:
        return None
    return re.sub(r'(?:this\.)?setMaxUpStep\(([^;]*)\);', lambda m: 'IafEntityUtil.setStepHeight(this, %s);' % m.group(1), line)


def r_ingredient(line, msg, block):
    if 'of(TagKey<Item>)' not in msg:
        return None
    return line.replace('Ingredient.of(', 'IafEntityUtil.ingredient(')


def r_particle_item(line, msg, block):
    if 'ItemParticleOption' not in msg:
        return None
    return re.sub(r'new ItemParticleOption\((ParticleTypes\.ITEM),\s*([^;]*?)\)(?=[,;)])',
                  lambda m: 'new ItemParticleOption(%s, ItemStackTemplate.fromNonEmptyStack(%s))' % (m.group(1), m.group(2)), line)


OPT = {'Integer': ('getInt', 'getIntOr', '0'), 'Boolean': ('getBoolean', 'getBooleanOr', 'false'),
       'Float': ('getFloat', 'getFloatOr', '0.0F'), 'Double': ('getDouble', 'getDoubleOr', '0.0D'),
       'Long': ('getLong', 'getLongOr', '0L'), 'Short': ('getShort', 'getShortOr', '(short) 0'),
       'Byte': ('getByte', 'getByteOr', '(byte) 0'), 'String': ('getString', 'getStringOr', '""')}


def r_optional(line, msg, block):
    m = re.search(r'Optional<(Integer|Boolean|Float|Double|Long|Short|Byte|String)> cannot be converted', msg)
    if not m:
        return None
    fn = OPT[m.group(1)]
    return re.sub(r'\.%s\(("[^"]*"|\w+)\)' % fn[0], lambda mm: '.%s(%s, %s)' % (fn[1], mm.group(1), fn[2]), line)


def r_create(line, msg, block):
    if not re.search(r'no suitable method found for create\((ServerLevel|Level)\)', msg):
        return None
    new = re.sub(r'\.create\(([^(),]+(?:\([^()]*\))?[^(),]*)\)', lambda mm: '.create(%s, EntitySpawnReason.EVENT)' % mm.group(1), line, count=1)
    return new if new != line else None


def r_knockback(line, msg, block):
    if not re.search(r'no suitable method found for knockback\((float|double),(float|double),(float|double)\)', msg):
        return None
    return re.sub(r'([\w.()]+?)\.knockback\(([^;]*)\);', lambda mm: 'IafEntityUtil.knockback(%s, %s);' % (mm.group(1), mm.group(2)), line)


RULES = [r_sound, r_spawn, r_sided, r_step, r_ingredient, r_particle_item, r_optional, r_create, r_knockback]

IMPORTS = [
    ('IafEntityUtil.', 'com.github.alexthe666.iceandfire.util.IafEntityUtil', 'com.github.alexthe666.iceandfire.util'),
    ('EntitySpawnReason.', 'net.minecraft.world.entity.EntitySpawnReason', None),
    ('ItemStackTemplate.', 'net.minecraft.world.item.ItemStackTemplate', None),
]

items = parse()
by_file = collections.defaultdict(list)
for path, ln, msg, block in items:
    by_file[path].append((ln, msg, block))

fixed = collections.Counter()
for path, errs in by_file.items():
    if not os.path.exists(path):
        continue
    lines = open(path, encoding='utf-8').read().split('\n')
    changed = False
    done = set()
    for ln, msg, block in errs:
        if ln in done or ln - 1 >= len(lines):
            continue
        line = lines[ln - 1]
        for rule in RULES:
            new = rule(line, msg, block)
            if new is not None and new != line:
                lines[ln - 1] = new
                line = new
                fixed[rule.__name__] += 1
                changed = True
        done.add(ln)
    if changed and not dry:
        text = '\n'.join(lines)
        for token, imp, own_pkg in IMPORTS:
            if token in text and ('import %s;' % imp) not in text and not (own_pkg and ('package %s;' % own_pkg) in text):
                text = re.sub(r'(package [\w.]+;\n)', lambda m: m.group(1) + '\nimport %s;' % imp, text, count=1)
        open(path, 'w', encoding='utf-8').write(text)
print(dict(fixed))
