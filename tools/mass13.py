"""Thirteenth mass pass: Forge events -> NeoForge events, EventBusSubscriber."""
import os
import re
import sys

dirs = [a for a in sys.argv[1:] if not a.startswith('--')] or ['src/main/java', 'src/client/java']


def matching_brace(text, open_idx):
    depth = 0
    for j in range(open_idx, len(text)):
        c = text[j]
        if c == '{':
            depth += 1
        elif c == '}':
            depth -= 1
            if depth == 0:
                return j
    return -1


def ensure_import(s, imp):
    if ('import %s;' % imp) in s:
        return s
    return re.sub(r'(package [\w.]+;\n)', lambda m: m.group(1) + '\nimport %s;' % imp, s, count=1)


def method_edit(s, header_regex, new_header, body_fn):
    out, pos = '', 0
    for m in re.finditer(header_regex, s):
        if m.start() < pos:
            continue
        end = matching_brace(s, m.end() - 1)
        if end < 0:
            continue
        out += s[pos:m.start()] + new_header(m) + body_fn(s[m.end():end], m)
        pos = end
    return out + s[pos:]


def fix(s):
    o = s
    # EventBusSubscriber
    def sub_annotation(m):
        args = m.group(1)
        keep = [a.strip() for a in re.split(r',\s*(?![^()]*\))', args) if a.strip().startswith(('modid', 'value'))]
        return '@EventBusSubscriber(%s)' % ', '.join(keep)
    if '@Mod.EventBusSubscriber' in s:
        s = re.sub(r'@Mod\.EventBusSubscriber\(([^)]*)\)', sub_annotation, s)
        s = ensure_import(s, 'net.neoforged.fml.common.EventBusSubscriber')
        s = s.replace('import net.neoforged.fml.common.Mod;\n', 'import net.neoforged.fml.common.Mod;\n') if '@Mod(' in s else s.replace('import net.neoforged.fml.common.Mod;\n', '')
    # LivingHurtEvent -> LivingDamageEvent.Pre
    if 'LivingHurtEvent' in s:
        s = method_edit(s, r'\(\s*(?:final )?LivingHurtEvent (\w+)\)\s*\{',
                        lambda m: '(LivingDamageEvent.Pre %s) {' % m.group(1),
                        lambda body, m: body.replace('%s.getAmount()' % m.group(1), '%s.getNewDamage()' % m.group(1))
                        .replace('%s.setAmount(' % m.group(1), '%s.setNewDamage(' % m.group(1)))
        s = s.replace('import net.neoforged.neoforge.event.entity.living.LivingHurtEvent;', 'import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;')
        s = re.sub(r'\bLivingHurtEvent\b', 'LivingDamageEvent.Pre', s)
    if 'LivingAttackEvent' in s:
        s = s.replace('import net.neoforged.neoforge.event.entity.living.LivingAttackEvent;', 'import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;')
        s = re.sub(r'\bLivingAttackEvent\b', 'LivingIncomingDamageEvent', s)
    if 'MobSpawnEvent.FinalizeSpawn' in s:
        s = s.replace('MobSpawnEvent.FinalizeSpawn', 'FinalizeSpawnEvent')
        s = ensure_import(s, 'net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent')
    s = re.sub(r'\.getOriginalTarget\(\)', '.getOriginalAboutToBeSetTarget()', s)
    s = re.sub(r'\.setNewTarget\(', '.setNewAboutToBeSetTarget(', s)
    # LivingTickEvent
    if 'LivingEvent.LivingTickEvent' in s or 'LivingTickEvent' in s:
        s = method_edit(s, r'\(\s*(?:final )?(?:LivingEvent\.)?LivingTickEvent (\w+)\)\s*\{',
                        lambda m: '(EntityTickEvent.Pre %s) {\n        if (!(%s.getEntity() instanceof net.minecraft.world.entity.LivingEntity iafLiving)) {\n            return;\n        }' % (m.group(1), m.group(1)),
                        lambda body, m: body.replace('%s.getEntity()' % m.group(1), 'iafLiving'))
        s = ensure_import(s, 'net.neoforged.neoforge.event.tick.EntityTickEvent')
        s = re.sub(r'import net\.neoforged\.neoforge\.event\.entity\.living\.LivingEvent;\n', '', s) if 'LivingEvent.' not in s else s
    if 'BlockEvent.BreakEvent' in s and 'event.level.BlockEvent' not in s:
        s = re.sub(r'import [\w.]*\.BlockEvent;', 'import net.neoforged.neoforge.event.level.BlockEvent;', s)
    return s


changed = 0
for d in dirs:
    for root, _, fs in os.walk(d):
        for f in fs:
            if not f.endswith('.java'):
                continue
            p = os.path.join(root, f)
            s = open(p, encoding='utf-8').read()
            n = fix(s)
            if n != s:
                open(p, 'w', encoding='utf-8').write(n)
                changed += 1
print('files changed:', changed)
