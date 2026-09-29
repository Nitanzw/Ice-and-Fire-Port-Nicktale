import os, re, sys

root = 'src/main/java'


def split_args(s, i):
    depth = 0
    args = []
    cur = ''
    j = i
    instr = False
    while j < len(s):
        c = s[j]
        if instr:
            cur += c
            if c == chr(92):
                cur += s[j + 1]
                j += 1
            elif c == '"':
                instr = False
        elif c == '"':
            instr = True
            cur += c
        elif c in '([{':
            depth += 1
            cur += c
        elif c in ')]}':
            if depth == 0:
                args.append(cur.strip())
                return args, j
            depth -= 1
            cur += c
        elif c == ',' and depth == 0:
            args.append(cur.strip())
            cur = ''
        else:
            cur += c
        j += 1
    return None, i


def fix_ident(s):
    out = ''
    pos = 0
    for m in re.finditer(r'new Identifier\(', s):
        if m.start() < pos:
            continue
        args, end = split_args(s, m.end())
        if args is None:
            continue
        if len(args) == 1:
            rep = 'Identifier.parse(%s)' % args[0]
        elif len(args) == 2:
            rep = 'Identifier.fromNamespaceAndPath(%s, %s)' % (args[0], args[1])
        else:
            continue
        out += s[pos:m.start()] + rep
        pos = end + 1
    return out + s[pos:]


n = 0
for d, _, fs in os.walk(root):
    for f in fs:
        if not f.endswith('.java'):
            continue
        p = os.path.join(d, f)
        s = open(p, encoding='utf-8').read()
        o = s
        s = fix_ident(s)
        s = re.sub(r'\.isClientSide\b(?!\s*\()', '.isClientSide()', s)
        s = re.sub(r'((?:level\(\)|[lL]evel\w*|[wW]orld\w*|p_\d+_))\.random\b', r'\1.getRandom()', s)
        s = s.replace('ForgeConfigSpec', 'ModConfigSpec')
        if s != o:
            open(p, 'w', encoding='utf-8').write(s)
            n += 1
print(n)
