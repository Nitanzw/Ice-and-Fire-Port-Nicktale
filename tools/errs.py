"""Summarize build/errors.txt. Usage: python tools/errs.py [prefix ...]  (file-name prefixes to focus on)"""
import collections
import os
import re
import sys

prefixes = tuple(sys.argv[1:]) or ('Model',)
lines = open('build/compile.log', encoding='utf-8-sig', errors='replace').read().splitlines()
byfile = collections.Counter()
msgs = collections.defaultdict(list)
for line in lines:
    m = re.match(r'(.*?\.java):(\d+): error: (.*)', line)
    if not m:
        continue
    name = os.path.basename(m.group(1).replace(chr(92), '/'))
    byfile[name] += 1
    if name.startswith(prefixes) or name.endswith('RenderState.java'):
        msgs[name].append((int(m.group(2)), m.group(3)))
print('total', sum(byfile.values()))
mine = {f: len(v) for f, v in msgs.items()}
print('focused', sum(mine.values()))
for f, c in sorted(mine.items(), key=lambda x: -x[1])[:40]:
    print(c, f)
kinds = collections.Counter(m for v in msgs.values() for _, m in v)
print()
for m, c in kinds.most_common(25):
    print(c, m[:130])
