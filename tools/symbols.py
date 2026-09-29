"""Top 'cannot find symbol' targets in build/compile.log with the number of files affected.
Usage: python tools/symbols.py [limit]"""
import collections
import re
import sys

limit = int(sys.argv[1]) if len(sys.argv) > 1 else 60
log = open('build/compile.log', encoding='utf-8-sig', errors='replace').read().split('\n')
count = collections.Counter()
files = collections.defaultdict(set)
loc = collections.defaultdict(collections.Counter)
i = 0
while i < len(log):
    m = re.search(r'([\w]+)\.java:\d+: error: cannot find symbol', log[i])
    if m:
        sym = ''
        location = ''
        for j in range(i + 1, min(i + 7, len(log))):
            t = log[j].strip()
            if t.startswith('symbol:') and not sym:
                sym = re.sub(r'\s+', ' ', t[7:].strip())[:70]
            if t.startswith('location:') and not location:
                location = re.sub(r'\s+', ' ', t[9:].strip())[:60]
        count[sym] += 1
        files[sym].add(m.group(1))
        loc[sym][location] += 1
    i += 1
for sym, c in count.most_common(limit):
    top = loc[sym].most_common(1)[0][0]
    print('%4d %3d files  %s   [%s]' % (c, len(files[sym]), sym, top))
