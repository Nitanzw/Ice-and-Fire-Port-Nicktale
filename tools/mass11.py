"""Eleventh mass pass: Fireball subclasses keep their own acceleration vector; adapt constructors to direction Vec3."""
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


def fix(s):
    if not re.search(r'extends Fireball\b', s) or 'xPower' not in s:
        return s
    o = s
    # own acceleration fields (the base class only has a single power value now)
    if 'protected double xPower' not in s:
        s = re.sub(r'(public (?:abstract )?class \w+ extends Fireball[^{]*\{\n)',
                   lambda m: m.group(1) + '\n    protected double xPower;\n    protected double yPower;\n    protected double zPower;\n', s, count=1)
    out, pos = '', 0
    for m in re.finditer(r'\bsuper\(', s):
        if m.start() < pos:
            continue
        end = call_end(s, m.end() - 1)
        parts = [p.strip() for p in split_top(s[m.end():end - 1])]
        if len(parts) == 8 and 'Vec3' not in parts[4]:
            new = ', '.join(parts[:4] + ['new Vec3(%s, %s, %s)' % (parts[4], parts[5], parts[6]), parts[7]])
        elif len(parts) == 6 and 'Vec3' not in ' '.join(parts) and not parts[2].strip().lstrip('-').replace('.','').isdigit() and 'posX' not in parts[1]:
            new = ', '.join([parts[0], parts[1], 'new Vec3(%s, %s, %s)' % (parts[2], parts[3], parts[4]), parts[5]])
        else:
            continue
        out += s[pos:m.end()] + new + ')'
        pos = end
    s = out + s[pos:]
    s = ensure_import(s, 'net.minecraft.world.phys.Vec3')
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
