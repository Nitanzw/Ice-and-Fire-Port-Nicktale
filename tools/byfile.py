"""Errors per file for a package prefix. Usage: python tools/byfile.py entity [limit]"""
import collections
import re
import sys

prefix = sys.argv[1].replace('/', '\\')
limit = int(sys.argv[2]) if len(sys.argv) > 2 else 60
by_file = collections.Counter()
for line in open('build/compile.log', encoding='utf-8-sig', errors='replace'):
    m = re.search(r'iceandfire[\\/](.+?)\.java:\d+: error', line)
    if not m:
        continue
    path = m.group(1).replace('/', '\\')
    if path.startswith(prefix):
        by_file[path] += 1
print(len(by_file), 'files,', sum(by_file.values()), 'errors')
for k, v in by_file.most_common(limit):
    print(v, k)
