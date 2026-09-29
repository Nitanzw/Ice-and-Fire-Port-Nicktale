"""Twelfth mass pass: AbstractArrow subclasses and shield checks."""
import os
import re
import sys

dirs = [a for a in sys.argv[1:] if not a.startswith('--')] or ['src/main/java']


def call_end(s, open_paren):
    depth = 1
    j = open_paren + 1
    while j < len(s) and depth:
        if s[j] == '(':
            depth += 1
        elif s[j] == ')':
            depth -= 1
        j += 1
    return j


def split_top(args):
    parts, depth, cur = [], 0, ''
    for c in args:
        if c in '([{':
            depth += 1
        elif c in ')]}':
            depth -= 1
        if c == ',' and depth == 0:
            parts.append(cur)
            cur = ''
            continue
        cur += c
    parts.append(cur)
    return parts


def ensure_import(s, imp):
    if ('import %s;' % imp) in s:
        return s
    return re.sub(r'(package [\w.]+;\n)', lambda m: m.group(1) + '\nimport %s;' % imp, s, count=1)


def fix_arrow(s):
    if not re.search(r'extends AbstractArrow\b', s):
        return s
    # the item the arrow gives back, taken from the old getPickupItem body
    m = re.search(r'ItemStack getPickupItem\(\)\s*\{\s*return (.*?);\s*\}', s, flags=re.S)
    stack_expr = m.group(1).strip() if m else 'new ItemStack(net.minecraft.world.item.Items.ARROW)'
    s = re.sub(r'(@NotNull\s+)?ItemStack getPickupItem\(\)', 'ItemStack getDefaultPickupItem()', s)
    out, pos = '', 0
    for cm in re.finditer(r'\bsuper\(', s):
        if cm.start() < pos:
            continue
        end = call_end(s, cm.end() - 1)
        parts = [p.strip() for p in split_top(s[cm.end():end - 1])]
        if len(parts) == 5 and parts[4] != 'null':       # (type, x, y, z, level)
            new = ', '.join(parts + [stack_expr, 'null'])
        elif len(parts) == 3 and parts[2].lower().endswith(('worldin', 'world', 'level')) and 'hooter' in parts[1].lower() or (len(parts) == 3 and parts[1].lower().startswith('shooter')):
            new = ', '.join([parts[0], parts[1], parts[2], stack_expr, 'null'])
        else:
            continue
        out += s[pos:cm.end()] + new + ')'
        pos = end
    s = out + s[pos:]
    s = re.sub(r'\bthis\.inGround\b', 'this.isInGround()', s)
    s = re.sub(r'!inGround\b', '!isInGround()', s)
    # baseDamage getter no longer exists: keep our own copy
    if 'getBaseDamage()' in s:
        s = re.sub(r'(?:this\.)?getBaseDamage\(\)', 'this.iafBaseDamage', s)
        s = re.sub(r'(public (?:abstract )?class \w+ extends AbstractArrow[^{]*\{\n)',
                   lambda mm: mm.group(1) + '\n    protected double iafBaseDamage = 2.0D;\n\n    @Override\n    public void setBaseDamage(double baseDamage) {\n        super.setBaseDamage(baseDamage);\n        this.iafBaseDamage = baseDamage;\n    }\n', s, count=1)
    # shield damage lambda
    s = re.sub(r'hurtAndBreak\((\w+), (\w+), \(\w+\) -> \{.*?\}\);', lambda mm: 'hurtAndBreak(%s, %s, %s.getUsedItemHand());' % (mm.group(1), mm.group(2), mm.group(2)), s, flags=re.S)
    return s


def fix_shield(s):
    return re.sub(r'([\w.()]+?)\.getItem\(\)\.canPerformAction\(([^,()]+(?:\(\))?), ItemAbilities\.SHIELD_BLOCK\)',
                  lambda m: '%s.has(net.minecraft.core.component.DataComponents.BLOCKS_ATTACKS)' % m.group(2), s)


changed = 0
for d in dirs:
    for root, _, fs in os.walk(d):
        for f in fs:
            if not f.endswith('.java'):
                continue
            p = os.path.join(root, f)
            s = open(p, encoding='utf-8').read()
            n = fix_shield(fix_arrow(s))
            if n != s:
                open(p, 'w', encoding='utf-8').write(n)
                changed += 1
print('files changed:', changed)
