"""Fifth mass pass: Entity.hurt -> hurtServer/IafDamage.hurt."""
import os
import re
import sys

dirs = [a for a in sys.argv[1:] if not a.startswith('--')] or ['src/main/java', 'src/client/java']
HELPER_IMPORT = 'import com.github.alexthe666.iceandfire.util.IafDamage;'


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
            # an identifier directly before '(' is a method call: keep walking
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


def fix_file(s):
    # declarations
    pos = 0
    out = ''
    for m in re.finditer(r'public boolean hurt\((?:@\w+ )?DamageSource (\w+), float (\w+)\)\s*\{', s):
        if m.start() < pos:
            continue
        end = matching_brace(s, m.end() - 1)
        if end < 0:
            continue
        body = s[m.end():end]
        body = re.sub(r'\bsuper\.hurt\(', 'super.hurtServer(level, ', body)
        out += s[pos:m.start()] + 'public boolean hurtServer(ServerLevel level, DamageSource %s, float %s) {' % (m.group(1), m.group(2)) + body
        pos = end
    out += s[pos:]
    s = out
    if 'hurtServer(ServerLevel level' in s and 'import net.minecraft.server.level.ServerLevel;' not in s:
        s = re.sub(r'(package [\w.]+;\n)', r'\1\nimport net.minecraft.server.level.ServerLevel;', s, count=1)
    # calls
    res = ''
    pos = 0
    for m in re.finditer(r'\.hurt\(', s):
        dot = m.start()
        if dot < pos:
            continue
        start = receiver_start(s, dot)
        recv = s[start:dot].strip()
        if not recv or recv == 'super' or recv.endswith('super') or recv == 'IafDamage':
            continue
        # skip declarations like "public boolean hurt(" (no dot) and matching only real calls
        res += s[pos:start] + 'IafDamage.hurt(' + recv + ', '
        pos = m.end()
    res += s[pos:]
    if res != s:
        s = res
        if HELPER_IMPORT not in s and 'package com.github.alexthe666.iceandfire.util;' not in s:
            s = re.sub(r'(package [\w.]+;\n)', r'\1\n' + HELPER_IMPORT, s, count=1)
    return s


changed = 0
for d in dirs:
    for root, _, fs in os.walk(d):
        for f in fs:
            if not f.endswith('.java'):
                continue
            p = os.path.join(root, f)
            s = open(p, encoding='utf-8').read()
            n = fix_file(s)
            if n != s:
                open(p, 'w', encoding='utf-8').write(n)
                changed += 1
print('files changed:', changed)
