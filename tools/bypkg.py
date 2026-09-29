"""Group compile errors from build/compile.log by package (and top files). Usage: python tools/bypkg.py [depth]"""
import collections
import re
import sys

depth = int(sys.argv[1]) if len(sys.argv) > 1 else 2
by_pkg = collections.Counter()
by_file = collections.Counter()
kinds = collections.Counter()
for line in open('build/compile.log', encoding='utf-8-sig', errors='replace'):
    m = re.search(r'iceandfire[\\/](.+?)\.java:\d+: error: (.*)', line)
    if not m:
        continue
    parts = re.split(r'[\\/]', m.group(1))
    by_pkg['/'.join(parts[:-1][:depth]) or '(root)'] += 1
    by_file[parts[-1]] += 1
    kinds[m.group(2)[:70]] += 1
print('total', sum(by_pkg.values()))
for k, v in by_pkg.most_common(30):
    print(v, k)
print()
for k, v in by_file.most_common(25):
    print(v, k)
print()
for k, v in kinds.most_common(15):
    print(v, k)
