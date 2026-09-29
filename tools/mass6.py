"""Sixth mass pass: NeoForge Tags names, game rules, home position API, owner uuid, misc small renames."""
import os
import re
import sys
import zipfile
import glob

dirs = [a for a in sys.argv[1:] if not a.startswith('--')] or ['src/main/java', 'src/client/java']
HOME = os.path.expanduser('~')

# ---- Tags.java constants per nested class
tags = {}
srcjar = glob.glob(HOME + '/.gradle/caches/modules-2/files-2.1/net.neoforged/neoforge/26.2.0.88/*/neoforge-26.2.0.88-sources.jar')
if srcjar:
    with zipfile.ZipFile(srcjar[0]) as z:
        text = z.read('net/neoforged/neoforge/common/Tags.java').decode('utf-8')
    cur = None
    for line in text.split('\n'):
        m = re.match(r'\s*public static class (\w+)', line)
        if m:
            cur = m.group(1)
            tags.setdefault(cur, set())
            continue
        m = re.match(r'\s*public static final TagKey<[^>]*(?:<[^>]*>)?> (\w+) =', line)
        if m and cur:
            tags[cur].add(m.group(1))


def fix_tags(s):
    def repl(m):
        cls, name = m.group(1), m.group(2)
        names = tags.get(cls)
        if names is None or name in names:
            return m.group(0)
        for cand in (name + 'S', name + 'ES', name[:-1] + 'IES' if name.endswith('Y') else None):
            if cand and cand in names:
                return 'Tags.%s.%s' % (cls, cand)
        return m.group(0)
    return re.sub(r'\bTags\.(\w+)\.([A-Z][A-Z0-9_]*)\b', repl, s)


RULES = {
    'RULE_DOENTITYDROPS': 'ENTITY_DROPS',
    'RULE_DOMOBLOOT': 'MOB_DROPS',
    'RULE_DOMOBSPAWNING': 'SPAWN_MOBS',
    'RULE_MOBGRIEFING': 'MOB_GRIEFING',
}


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


def rewrite_calls(s, method, builder):
    """Replace 'recv.method(args)' using builder(recv, args_text)."""
    res = ''
    pos = 0
    for m in re.finditer(r'\.' + method + r'\(', s):
        dot = m.start()
        if dot < pos:
            continue
        start = receiver_start(s, dot)
        recv = s[start:dot].strip()
        if not recv or recv == 'super':
            continue
        # args up to matching paren
        depth = 1
        j = m.end()
        while j < len(s) and depth:
            if s[j] == '(':
                depth += 1
            elif s[j] == ')':
                depth -= 1
            j += 1
        args = s[m.end():j - 1]
        res += s[pos:start] + builder(recv, args)
        pos = j
    return res + s[pos:]


def fix_gamerules(s):
    def build(recv, args):
        return 'IafEntityUtil.gameRule(%s, GameRules.%s)' % (recv, RULES.get(args.strip().split('.')[-1], args.strip().split('.')[-1]))
    # "recv.getGameRules().getBoolean(X)" : first handle the getBoolean call whose receiver is recv.getGameRules()
    def build2(recv, args):
        r = recv
        if r.endswith('.getGameRules()'):
            r = r[:-len('.getGameRules()')]
        elif r.endswith('getGameRules()'):
            r = r[:-len('getGameRules()')] or 'this'
        else:
            return None
        name = args.strip().split('.')[-1]
        return 'IafEntityUtil.gameRule(%s, GameRules.%s)' % (r, RULES.get(name, name))
    res = ''
    pos = 0
    for m in re.finditer(r'\.getBoolean\(', s):
        dot = m.start()
        if dot < pos:
            continue
        start = receiver_start(s, dot)
        recv = s[start:dot].strip()
        if 'getGameRules()' not in recv:
            continue
        depth = 1
        j = m.end()
        while j < len(s) and depth:
            if s[j] == '(':
                depth += 1
            elif s[j] == ')':
                depth -= 1
            j += 1
        args = s[m.end():j - 1]
        out = build2(recv, args)
        if out is None:
            continue
        res += s[pos:start] + out
        pos = j
    return res + s[pos:]


SIMPLE = [
    (r'\.hasImpulse\b', '.hurtMarked'),
    (r'\bthis\.hasImpulse\b', 'this.hurtMarked'),
    (r'\bgetRestrictCenter\(\)', 'getHomePosition()'),
    (r'\bhasRestriction\(\)', 'hasHome()'),
    (r'\bisWithinRestriction\(', 'isWithinHome('),
    (r'\brestrictTo\(', 'setHomeTo('),
    (r'\bgetRestrictRadius\(\)', 'getHomeRadius()'),
    (r'\bclearRestriction\(\)', 'clearHome()'),
    (r'\bthis\.getServer\(\)', 'this.level().getServer()'),
    (r'^\s*this\.noCulling = true;\s*$', '        // noCulling was removed from Entity in 1.21'),
]


def fix_owner(s, path):
    if 'UUID getOwnerUUID' in s:
        return s
    # bare and this. calls inside tamable subclasses
    s = re.sub(r'(?<![\w.])this\.getOwnerUUID\(\)', 'IafEntityUtil.ownerUUID(this)', s)
    s = re.sub(r'(?<![\w.])(?<!UUID )getOwnerUUID\(\)', 'IafEntityUtil.ownerUUID(this)', s)
    return rewrite_calls(s, 'getOwnerUUID', lambda r, a: 'IafEntityUtil.ownerUUID(%s)' % r)


def ensure_import(s, imp):
    if ('import %s;' % imp) in s:
        return s
    if ('package %s;' % imp.rsplit('.', 1)[0]) in s:
        return s
    return re.sub(r'(package [\w.]+;\n)', r'\1\nimport %s;' % imp, s, count=1)


changed = 0
for d in dirs:
    for root, _, fs in os.walk(d):
        for f in fs:
            if not f.endswith('.java'):
                continue
            p = os.path.join(root, f)
            s = open(p, encoding='utf-8').read()
            o = s
            s = fix_tags(s)
            s = fix_gamerules(s)
            for pat, rep in SIMPLE:
                s = re.sub(pat, rep, s, flags=re.M)
            s = fix_owner(s, p)
            if 'IafEntityUtil.' in s and 'IafEntityUtil.java' not in f:
                s = ensure_import(s, 'com.github.alexthe666.iceandfire.util.IafEntityUtil')
            if 'GameRules.' in s and 'IafEntityUtil.gameRule' in s:
                s = ensure_import(s, 'net.minecraft.world.level.gamerules.GameRules')
                s = s.replace('import net.minecraft.world.level.GameRules;\n', '')
            if s != o:
                open(p, 'w', encoding='utf-8').write(s)
                changed += 1
print('files changed:', changed)
