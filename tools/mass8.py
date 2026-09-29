"""Eighth mass pass: changed method signatures on Mob/LivingEntity overrides (server level parameter) and small renames."""
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


def call_args(s, open_paren):
    depth = 1
    j = open_paren + 1
    while j < len(s) and depth:
        if s[j] == '(':
            depth += 1
        elif s[j] == ')':
            depth -= 1
        j += 1
    return s[open_paren + 1:j - 1], j


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
        else:
            cur += c
    parts.append(cur)
    return parts


def rename_body(s, header_regex, new_header, body_subs):
    """Rewrites a method declaration and applies body_subs inside it."""
    out, pos = '', 0
    for m in re.finditer(header_regex, s):
        if m.start() < pos:
            continue
        end = matching_brace(s, m.end() - 1)
        if end < 0:
            continue
        body = s[m.end():end]
        for pat, rep in body_subs:
            body = re.sub(pat, rep, body)
        out += s[pos:m.start()] + new_header(m) + body
        pos = end
    return out + s[pos:]


def ensure_import(s, imp):
    if ('import %s;' % imp) in s or ('package %s;' % imp.rsplit('.', 1)[0]) in s:
        return s
    return re.sub(r'(package [\w.]+;\n)', r'\1\nimport %s;' % imp, s, count=1)


def fix(s):
    o = s
    # doHurtTarget(Entity x) override -> doHurtTarget(ServerLevel level, Entity x)
    s = rename_body(s, r'public boolean doHurtTarget\((@\w+ )?(?:final )?Entity (\w+)\)\s*\{',
                    lambda m: 'public boolean doHurtTarget(ServerLevel level, %sEntity %s) {' % (m.group(1) or '', m.group(2)),
                    [(r'super\.doHurtTarget\((\w+)\)', r'super.doHurtTarget(level, \1)')])
    # getExperienceReward()
    s = rename_body(s, r'public int getExperienceReward\(\)\s*\{',
                    lambda m: 'protected int getBaseExperienceReward(ServerLevel level) {',
                    [(r'super\.getExperienceReward\(\)', 'super.getBaseExperienceReward(level)')])
    # isInvulnerableTo(DamageSource)
    s = rename_body(s, r'public boolean isInvulnerableTo\((@\w+ )?(?:final )?DamageSource (\w+)\)\s*\{',
                    lambda m: 'public boolean isInvulnerableTo(ServerLevel level, %sDamageSource %s) {' % (m.group(1) or '', m.group(2)),
                    [(r'super\.isInvulnerableTo\((\w+)\)', r'super.isInvulnerableTo(level, \1)')])
    # customServerAiStep()
    s = rename_body(s, r'protected void customServerAiStep\(\)\s*\{',
                    lambda m: 'protected void customServerAiStep(ServerLevel level) {',
                    [(r'super\.customServerAiStep\(\)', 'super.customServerAiStep(level)')])
    if s != o:
        s = ensure_import(s, 'net.minecraft.server.level.ServerLevel')
    o2 = s
    # call sites of doHurtTarget with a single argument -> helper
    res, pos = '', 0
    for m in re.finditer(r'\bdoHurtTarget\(', s):
        if m.start() < pos:
            continue
        args, j = call_args(s, m.end() - 1)
        if len(split_top(args)) != 1:
            continue
        # skip declarations/super calls handled above
        before = s[max(0, m.start() - 6):m.start()]
        if before.endswith('super.'):
            continue
        prev = s[:m.start()].rstrip()
        if prev.endswith(('boolean', 'public boolean', 'protected boolean')):
            continue
        if m.start() > 0 and s[m.start() - 1] == '.':
            start = receiver_start(s, m.start() - 1)
            recv = s[start:m.start() - 1].strip()
            res += s[pos:start] + 'IafEntityUtil.attack(%s, %s)' % (recv, args.strip())
        else:
            res += s[pos:m.start()] + 'IafEntityUtil.attack(this, %s)' % args.strip()
        pos = j
    res += s[pos:]
    s = res
    # simple renames
    s = re.sub(r'\bisControlledByLocalInstance\(\)', 'isLocalInstanceAuthoritative()', s)
    s = re.sub(r'(?<![\w.])this\.tryCheckInsideBlocks\(\)', 'this.applyEffectsFromBlocks()', s)
    s = re.sub(r'(?<![\w.])tryCheckInsideBlocks\(\)', 'applyEffectsFromBlocks()', s)
    s = re.sub(r'\b(?:\w+(?:\(\))?\.)*\w+(?:\(\))?\.getProfiler\(\)', 'net.minecraft.util.profiling.Profiler.get()', s)
    s = re.sub(r'\.getTags\(\)\.contains', '.entityTags().contains', s)
    # absMoveTo/moveTo with five arguments -> snapTo
    res, pos = '', 0
    for m in re.finditer(r'\.(absMoveTo|moveTo)\(', s):
        if m.start() < pos:
            continue
        args, j = call_args(s, m.end() - 1)
        if len(split_top(args)) == 5:
            res += s[pos:m.start()] + '.snapTo(' + args + ')'
            pos = j
    res += s[pos:]
    s = res
    if 'IafEntityUtil.' in s and 'package com.github.alexthe666.iceandfire.util;' not in s:
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
