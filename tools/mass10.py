"""Tenth mass pass: RegistryObject, entity type tags, registry access, difficulty, selectors in NearestAttackableTargetGoal subclasses."""
import os
import re
import sys

dirs = [a for a in sys.argv[1:] if not a.startswith('--')] or ['src/main/java', 'src/client/java']


def ensure_import(s, imp):
    if ('import %s;' % imp) in s or ('package %s;' % imp.rsplit('.', 1)[0]) in s:
        return s
    return re.sub(r'(package [\w.]+;\n)', lambda m: m.group(1) + '\nimport %s;' % imp, s, count=1)


def receiver_start(text, dot):
    i = dot - 1
    while i >= 0:
        c = text[i]
        if c == ')':
            depth = 0
            while i >= 0:
                if text[i] == ')':
                    depth += 1
                elif text[i] == '(':
                    depth -= 1
                    if depth == 0:
                        break
                i -= 1
            i -= 1
            if i >= 0 and (text[i].isalnum() or text[i] == '_'):
                while i >= 0 and (text[i].isalnum() or text[i] == '_'):
                    i -= 1
            else:
                return i + 1
        elif c.isalnum() or c == '_':
            while i >= 0 and (text[i].isalnum() or text[i] == '_'):
                i -= 1
        else:
            return i + 1
        if i >= 0 and text[i] == '.':
            i -= 1
            continue
        return i + 1
    return 0


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


def fix_difficulty(s):
    out, pos = '', 0
    for m in re.finditer(r'\.getCurrentDifficultyAt\(', s):
        if m.start() < pos:
            continue
        start = receiver_start(s, m.start())
        recv = s[start:m.start()].strip()
        end = call_end(s, m.end() - 1)
        args = s[m.end():end - 1]
        if not recv or recv.endswith('ServerLevel') or 'IafEntityUtil' in recv:
            continue
        out += s[pos:start] + 'IafEntityUtil.difficulty(%s, %s)' % (recv, args)
        pos = end
    return out + s[pos:]


def fix_super_selector(s):
    if 'extends NearestAttackableTargetGoal' not in s:
        return s
    out, pos = '', 0
    for m in re.finditer(r'\bsuper\(', s):
        if m.start() < pos:
            continue
        end = call_end(s, m.end() - 1)
        parts = split_top(s[m.end():end - 1])
        if len(parts) == 6:
            last = parts[5].strip()
            if 'IafEntityUtil.selector' not in last and '->' not in last and last != 'null':
                parts[5] = ' IafEntityUtil.selector(%s)' % last
                out += s[pos:m.end()] + ','.join(parts) + ')'
                pos = end
    return out + s[pos:]


def fix(s):
    o = s
    # RegistryObject<T> -> Supplier<T>
    if 'RegistryObject' in s:
        s = re.sub(r'\bRegistryObject<', 'Supplier<', s)
        s = s.replace('import net.neoforged.neoforge.registries.RegistryObject;', 'import java.util.function.Supplier;')
        if 'import java.util.function.Supplier;' not in s:
            s = ensure_import(s, 'java.util.function.Supplier')
    s = re.sub(r'\.getType\(\)\.is\(', '.getType().builtInRegistryHolder().is(', s)
    s = re.sub(r'\.registryOrThrow\(', '.lookupOrThrow(', s)
    s = fix_difficulty(s)
    s = fix_super_selector(s)
    s = re.sub(r'EntityType\.byString\(', 'IafEntityUtil.entityTypeByString(', s)
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
