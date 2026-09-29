"""Collapse IafEntityUtil.selector(IafEntityUtil.selector(X)) into IafEntityUtil.selector(X)."""
import os
import sys

TOKEN = 'IafEntityUtil.selector('
changed = 0
for d in ['src/main/java', 'src/client/java']:
    for root, _, fs in os.walk(d):
        for f in fs:
            if not f.endswith('.java'):
                continue
            p = os.path.join(root, f)
            s = open(p, encoding='utf-8').read()
            o = s
            while True:
                i = s.find(TOKEN + TOKEN)
                if i < 0:
                    break
                # find the closing paren of the outer call
                depth = 1
                j = i + len(TOKEN)
                while j < len(s) and depth:
                    if s[j] == '(':
                        depth += 1
                    elif s[j] == ')':
                        depth -= 1
                    j += 1
                # s[j-1] is the outer ')'; drop the outer token and that paren
                s = s[:i] + s[i + len(TOKEN):j - 1] + s[j:]
            if s != o:
                open(p, 'w', encoding='utf-8').write(s)
                changed += 1
print('files changed:', changed)
