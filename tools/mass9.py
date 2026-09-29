"""Ninth mass pass: custom data serializers, targeting selectors."""
import os
import re
import sys

dirs = [a for a in sys.argv[1:] if not a.startswith('--')] or ['src/main/java', 'src/client/java']


def call_args_end(s, open_paren):
    depth = 1
    j = open_paren + 1
    while j < len(s) and depth:
        c = s[j]
        if c == '(':
            depth += 1
        elif c == ')':
            depth -= 1
        j += 1
    return j


def split_top(args):
    parts, depth, cur, instr = [], 0, '', False
    for c in args:
        if c == '"':
            instr = not instr
        if not instr:
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
    if ('import %s;' % imp) in s or ('package %s;' % imp.rsplit('.', 1)[0]) in s:
        return s
    return re.sub(r'(package [\w.]+;\n)', lambda m: m.group(1) + '\nimport %s;' % imp, s, count=1)


def wrap_selector_args(s):
    out, pos = '', 0
    for m in re.finditer(r'new NearestAttackableTargetGoal(?:<[^>]*>)?\(', s):
        if m.start() < pos:
            continue
        end = call_args_end(s, m.end() - 1)
        args = s[m.end():end - 1]
        parts = split_top(args)
        if len(parts) == 6:
            last = parts[5].strip()
            if '->' not in last and 'IafEntityUtil.selector' not in last and 'null' != last:
                parts[5] = ' IafEntityUtil.selector(%s)' % last
                out += s[pos:m.end()] + ','.join(parts) + ')'
                pos = end
    out += s[pos:]
    s = out
    out, pos = '', 0
    for m in re.finditer(r'\.selector\(', s):
        if m.start() < pos:
            continue
        end = call_args_end(s, m.end() - 1)
        arg = s[m.end():end - 1].strip()
        if arg and '->' not in arg and 'IafEntityUtil.selector' not in arg and arg != 'null':
            out += s[pos:m.end()] + 'IafEntityUtil.selector(%s)' % arg + ')'
            pos = end
    out += s[pos:]
    return out


def fix(s):
    o = s
    s = s.replace('EntityDataSerializers.OPTIONAL_UUID', 'IafDataSerializers.OPTIONAL_UUID')
    s = s.replace('EntityDataSerializers.COMPOUND_TAG', 'IafDataSerializers.COMPOUND_TAG')
    if 'IafDataSerializers.' in s:
        s = ensure_import(s, 'com.github.alexthe666.iceandfire.misc.IafDataSerializers')
    s = wrap_selector_args(s)
    if 'IafEntityUtil.' in s:
        s = ensure_import(s, 'com.github.alexthe666.iceandfire.util.IafEntityUtil')
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
