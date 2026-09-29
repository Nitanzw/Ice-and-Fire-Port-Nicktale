"""Print compile errors for files whose name contains any of the given fragments. Usage: python tools/errsfor.py Frag1 Frag2 ..."""
import re
import sys

frags = sys.argv[1:]
log = open('build/compile.log', encoding='utf-8-sig', errors='replace').read().split('\n')
count = 0
for i, l in enumerate(log):
    m = re.search(r'([\w]+\.java):(\d+): error: (.*)', l)
    if m and any(f in m.group(1) for f in frags):
        nxt = log[i + 1].strip()[:110] if i + 1 < len(log) else ''
        print(m.group(1), m.group(2), m.group(3)[:100], '|', nxt)
        count += 1
print('matches', count)
