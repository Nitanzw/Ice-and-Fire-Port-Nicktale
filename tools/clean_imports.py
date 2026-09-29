"""Removes imports whose simple name is not used in the file. Usage: python tools/clean_imports.py dir [dir ...]"""
import os
import re
import sys

for root in sys.argv[1:]:
    for d, _, fs in os.walk(root):
        for f in fs:
            if not f.endswith('.java'):
                continue
            p = os.path.join(d, f)
            s = open(p, encoding='utf-8').read()
            lines = s.split('\n')
            body = '\n'.join(l for l in lines if not l.startswith('import '))
            out = []
            seen = set()
            for l in lines:
                m = re.match(r'import (?:static )?([\w.]+)\.(\w+|\*);', l)
                if m and m.group(2) != '*':
                    if l in seen:
                        continue
                    seen.add(l)
                    if not re.search(r'\b' + m.group(2) + r'\b', body):
                        continue
                out.append(l)
            n = '\n'.join(out)
            if n != s:
                open(p, 'w', encoding='utf-8').write(n)
