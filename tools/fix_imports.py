"""Rewrites imports of Minecraft/NeoForge classes that moved packages between 1.20 and 26.2.

Source of truth: the class lists of the patched Minecraft jar and the NeoForge universal jar.
An import is rewritten only when its simple name has exactly one plausible home (or one clearly closest home).
Usage: python tools/fix_imports.py [--dry] [src_dir ...]
"""
import collections
import glob
import os
import re
import sys
import zipfile

HOME = os.path.expanduser('~')
MC_JAR = os.environ.get('MC_PATCHED_JAR', 'nicktale-api/build/moddev/artifacts/minecraft-patched-26.2.0.88.jar')
JARS = [MC_JAR] + glob.glob(HOME + '/.gradle/caches/modules-2/files-2.1/net.neoforged/neoforge/26.2.0.88/*/neoforge-26.2.0.88-universal.jar') \
    + [j for j in glob.glob(HOME + '/.gradle/caches/modules-2/files-2.1/net.neoforged*/**/*.jar', recursive=True)
       if not j.endswith(('-sources.jar', '-javadoc.jar', '-userdev.jar'))]
CHECK_PREFIXES = ('net.minecraft.', 'net.neoforged.', 'com.mojang.blaze3d.')

known = set()
by_simple = collections.defaultdict(set)


def add_class(fqn):
    known.add(fqn)
    by_simple[fqn.rsplit('.', 1)[-1]].add(fqn)


for jar in JARS:
    if not os.path.exists(jar):
        continue
    with zipfile.ZipFile(jar) as z:
        for n in z.namelist():
            if n.endswith('.class') and not n.startswith('META-INF'):
                add_class(n[:-6].replace('/', '.').replace('$', '.'))

dry = '--dry' in sys.argv
dirs = [a for a in sys.argv[1:] if not a.startswith('--')] or ['src/main/java', 'src/client/java']

# classes defined by the project itself
own = set()
for d in dirs:
    for root, _, fs in os.walk(d):
        for f in fs:
            if f.endswith('.java'):
                rel = os.path.relpath(os.path.join(root, f), d)
                own.add(rel[:-5].replace(os.sep, '.'))
known |= own


def common_prefix_len(a, b):
    pa, pb = a.split('.'), b.split('.')
    n = 0
    for x, y in zip(pa, pb):
        if x != y:
            break
        n += 1
    return n


fixed = 0
unresolved = collections.Counter()
ambiguous = {}
for d in dirs:
    for root, _, fs in os.walk(d):
        for f in fs:
            if not f.endswith('.java'):
                continue
            p = os.path.join(root, f)
            s = open(p, encoding='utf-8').read()
            out = []
            changed = False
            for line in s.split('\n'):
                m = re.match(r'import (static )?([\w.]+)\.(\w+|\*);', line)
                if m and not m.group(1) and m.group(3) != '*' and m.group(2).startswith(CHECK_PREFIXES):
                    fqn = m.group(2) + '.' + m.group(3)
                    if fqn not in known:
                        cands = sorted(by_simple.get(m.group(3), ()))
                        cands = [c for c in cands if c.startswith(CHECK_PREFIXES)]
                        if len(cands) == 1:
                            line = 'import %s;' % cands[0]
                            changed = True
                            fixed += 1
                        elif len(cands) > 1:
                            best = max(common_prefix_len(fqn, c) for c in cands)
                            top = [c for c in cands if common_prefix_len(fqn, c) == best]
                            if len(top) == 1:
                                line = 'import %s;' % top[0]
                                changed = True
                                fixed += 1
                            else:
                                ambiguous[fqn] = cands
                        else:
                            unresolved[fqn] += 1
                out.append(line)
            if changed and not dry:
                open(p, 'w', encoding='utf-8').write('\n'.join(out))
print('rewritten imports:', fixed)
print('ambiguous:', len(ambiguous))
for k, v in sorted(ambiguous.items())[:40]:
    print('  ', k, '->', v[:4])
print('unresolved (no class with that name in MC/NeoForge):', len(unresolved))
for k, v in unresolved.most_common(60):
    print('  ', v, k)
