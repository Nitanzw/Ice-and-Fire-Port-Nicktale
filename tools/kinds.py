"""Most common normalized diagnostics with one sample source line each. Usage: python tools/kinds.py [limit]"""
import collections
import re
import sys

limit = int(sys.argv[1]) if len(sys.argv) > 1 else 40
log = open('build/compile.log', encoding='utf-8-sig', errors='replace').read().split('\n')
cnt = collections.Counter()
sample = {}
files = collections.defaultdict(set)
i = 0
while i < len(log):
    m = re.match(r'.+?[\\/](\w+)\.java:(\d+): error: (.*)', log[i])
    if m:
        msg = m.group(3)
        sym = ''
        src = log[i + 1].strip()[:110] if i + 1 < len(log) else ''
        for k in range(i + 1, min(i + 8, len(log))):
            t = log[k].strip()
            if t.startswith('symbol:'):
                sym = re.sub(r'\s+', ' ', t)[7:].strip()[:50]
                break
        key = re.sub(r'\b\w+Entity\w*\b', 'X', msg)[:70]
        if msg.startswith('cannot find symbol'):
            key = 'symbol: ' + sym
        cnt[key] += 1
        files[key].add(m.group(1))
        sample.setdefault(key, '%s:%s  %s' % (m.group(1), m.group(2), src))
        i += 1
    else:
        i += 1
for k, c in cnt.most_common(limit):
    print('%4d %2d files  %s\n        %s' % (c, len(files[k]), k, sample[k]))
