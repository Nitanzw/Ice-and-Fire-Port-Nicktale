"""Print full javac diagnostics (message, source line, symbol) for files matching fragments.
Usage: python tools/errdetail.py EntityDragonCharge [Other ...]"""
import re
import sys

frags = sys.argv[1:]
log = open('build/compile.log', encoding='utf-8-sig', errors='replace').read().split('\n')
i = 0
while i < len(log):
    m = re.search(r'([\w]+)\.java:(\d+): error: (.*)', log[i])
    if m and any(m.group(1) == f or m.group(1).startswith(f) for f in frags):
        block = [m.group(1) + ':' + m.group(2) + ' ' + m.group(3)[:110]]
        j = i + 1
        while j < len(log) and not re.search(r'\.java:\d+: error:', log[j]) and j < i + 7:
            t = log[j].strip()
            if t and not t.startswith('^'):
                block.append('    ' + t[:120])
            j += 1
        print('\n'.join(block))
        i = j
    else:
        i += 1
