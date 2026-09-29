"""Converts Ice and Fire 1.20 entity-based models to NeoForge 26.x render-state models.

Usage: python tools/port_models.py ModelName [ModelName ...]
For each model it rewrites client/model/<ModelName>.java, writes <State>RenderState.java and prints the
extraction snippet the renderer needs. Anything it cannot resolve is printed as TODO for manual work.
"""
import os
import re
import sys

BASE = 'src/main/java/com/github/alexthe666/iceandfire'
MODEL_DIR = os.path.join(BASE, 'client', 'model')
ENTITY_DIR = os.path.join(BASE, 'entity')

BUILTIN = {
    'getAnimation': ('animation', 'entity.getAnimation()'),
    'getAnimationTick': ('animationTick', 'entity.getAnimationTick() + partialTick'),
}


def read(path):
    with open(path, encoding='utf-8') as f:
        return f.read()


def write(path, text):
    with open(path, 'w', encoding='utf-8') as f:
        f.write(text)


_entity_cache = {}


def entity_source(name):
    if name in _entity_cache:
        return _entity_cache[name]
    for d, _, fs in os.walk(ENTITY_DIR):
        if name + '.java' in fs:
            _entity_cache[name] = read(os.path.join(d, name + '.java'))
            return _entity_cache[name]
    _entity_cache[name] = None
    return None


def member_type(entity, member, is_call, depth=0):
    """Returns the Java type of entity.member (walking superclasses), or None."""
    if depth > 8 or entity is None:
        return None
    src = entity_source(entity)
    if src is None:
        return None
    if is_call:
        m = re.search(r'(?:public|protected)\s+(?:final\s+)?([\w<>\[\]?,. ]+?)\s+' + re.escape(member) + r'\s*\(\s*\)', src)
    else:
        m = re.search(r'(?:public|protected)\s+(?:final\s+)?(?:static\s+)?([\w<>\[\]?,. ]+?)\s+' + re.escape(member) + r'\s*[;=]', src)
    if m:
        t = m.group(1).strip()
        if t not in ('return', 'new', 'class'):
            return t
    sup = re.search(r'class\s+' + re.escape(entity) + r'[^{]*?extends\s+(\w+)', src)
    if sup:
        return member_type(sup.group(1), member, is_call, depth + 1)
    return None


BASE_STATE = {'isInWater', 'deathTime', 'isUpsideDown', 'isBaby'}

KNOWN = {
    # member -> (state field type, extraction expression suffix)
    'onGround': ('boolean', 'onGround()'),
    'isAlive': ('boolean', 'isAlive()'),
    'tickCount': ('int', 'tickCount'),
}


def collect_imports(entity, depth=0):
    """simple type name -> import line, for the entity class and its superclasses."""
    out = {}
    if depth > 8 or entity is None:
        return out
    src = entity_source(entity)
    if src is None:
        return out
    for im in re.finditer(r'^import ((?:static )?[\w.]+\.(\w+));', src, flags=re.M):
        out.setdefault(im.group(2), im.group(0))
    sup = re.search(r'class\s+' + re.escape(entity) + r'[^{]*?extends\s+(\w+)', src)
    if sup:
        for k, v in collect_imports(sup.group(1), depth + 1).items():
            out.setdefault(k, v)
    return out


def remove_method(text, signature_regex):
    """Removes the (optionally @Override-annotated) method whose header matches signature_regex."""
    while True:
        m = re.search(r'(?:[ \t]*@Override\s*\n)?[ \t]*' + signature_regex + r'[^{;]*\{', text)
        if not m:
            return text
        i = m.end()
        depth = 1
        while depth and i < len(text):
            c = text[i]
            if c == '{':
                depth += 1
            elif c == '}':
                depth -= 1
            i += 1
        end = i
        while end < len(text) and text[end] in ' \t':
            end += 1
        if end < len(text) and text[end] == '\n':
            end += 1
        text = text[:m.start()] + text[end:]


def convert(name):
    path = os.path.join(MODEL_DIR, name + '.java')
    text = read(path)
    m = re.search(r'class\s+' + name + r'\s+extends\s+(\w+)<(\w+)>', text)
    ent = None
    state = 'IafRenderState'
    myrmex = name.startswith('ModelMyrmex') and name != 'ModelMyrmexBase'
    if myrmex:
        kind = name[len('ModelMyrmex'):]
        text = re.sub(r'class\s+' + name + r'\s+extends\s+(\w+)\s*\{', 'class ' + name + r' extends <MyrmexRenderState> {', text)
        ent = 'EntityMyrmex' + kind
        state = 'MyrmexRenderState'
        m = None
    if m and m.group(2).startswith('Entity'):
        ent = m.group(2)
        state = ent[len('Entity'):] + 'RenderState'
        text = text.replace(m.group(0), 'class %s extends %s<%s>' % (name, m.group(1), state))
    elif m:
        text = text.replace(m.group(0), 'class %s extends %s<%s>' % (name, m.group(1), state))
    else:
        m2 = re.search(r'class\s+' + name + r'\s+extends\s+(\w+)\s*\{', text)
        if m2:
            text = text.replace(m2.group(0), 'class %s extends %s<%s> {' % (name, m2.group(1), state))

    # drop obsolete overrides
    text = remove_method(text, r'public\s+void\s+renderStatue\s*\(')
    text = remove_method(text, r'public\s+Iterable<\w+>\s+parts\s*\(')
    text = remove_method(text, r'public\s+Iterable<\w+>\s+getAllParts\s*\(')

    # setupAnim -> animate(State)
    var = None
    sm = re.search(r'public\s+void\s+setupAnim\s*\(\s*(?:@\w+\s+)?[\w<>]+\s+(\w+)\s*,\s*float\s+(\w+)\s*,\s*float\s+(\w+)\s*,\s*float\s+(\w+)\s*,\s*float\s+(\w+)\s*,\s*float\s+(\w+)\s*\)\s*\{', text)
    if sm:
        var = sm.group(1)
        f, f1, f2, f3, f4 = sm.group(2, 3, 4, 5, 6)
        head = ('protected void animate(%s %s) {\n'
                '        float %s = %s.walkAnimationPos;\n'
                '        float %s = %s.walkAnimationSpeed;\n'
                '        float %s = %s.ageInTicks;\n'
                '        float %s = %s.yRot;\n'
                '        float %s = %s.xRot;\n') % (state, var, f, var, f1, var, f2, var, f3, var, f4, var)
        text = text[:sm.start()] + head + text[sm.end():]
        # remove the @Override that belonged to setupAnim only if it stays valid: keep it (abstract animate)
    # helper animate(IAnimatedEntity ...)
    text = re.sub(r'animate\(\s*IAnimatedEntity\s+(\w+)\s*,', r'animate(%s \1,' % state, text)
    text = re.sub(r'animate\(\s*Entity\w+\s+(\w+)\s*,\s*float', r'animate(%s \1, float' % state, text)
    text = re.sub(r'animate\(\(IAnimatedEntity\)\s*(\w+)\s*,', r'animate(\1,', text)
    text = re.sub(r'animator\.update\((\w+)\)', r'animator.update(\1.animation, \1.animationTick)', text)

    text = text.replace('Minecraft.getInstance().getFrameTime()',
                        'Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(false)')
    # collect entity variable names
    names = set()
    if var:
        names.add(var)
    # aliases such as "EntityHippocampus hippo = entity;" are folded into the state variable
    for am in list(re.finditer(r'\n[ \t]*Entity\w+\s+(\w+)\s*=\s*(?:\(\s*Entity\w+\s*\)\s*)?(\w+);[ \t]*(?=\n)', text)):
        alias, source = am.group(1), am.group(2)
        if source in names or source == var:
            text = text.replace(am.group(0), '', 1)
            text = re.sub(r'\b' + re.escape(alias) + r'\b', source, text)
    for hm in re.finditer(r'animate\(%s (\w+)' % re.escape(state), text):
        names.add(hm.group(1))
    # analyse code only: no imports, no comments
    code = re.sub(r'/\*.*?\*/', '', text, flags=re.S)
    code = re.sub(r'//[^\n]*', '', code)
    code = re.sub(r'^import [^\n]*\n', '', code, flags=re.M)
    members = {}
    manual = set()
    for v in names:
        for mm in re.finditer(r'(?<![\w.])' + re.escape(v) + r'\.(\w+)(\(\s*\))?(\()?', code):
            member, call, withargs = mm.group(1), bool(mm.group(2)), bool(mm.group(3))
            if member in ('walkAnimationPos', 'walkAnimationSpeed', 'ageInTicks', 'yRot', 'xRot', 'animation', 'animationTick', 'isBaby'):
                continue
            if withargs:
                manual.add(member)
                continue
            members[(member, call)] = None
    fields = []
    todo = sorted(manual)
    for (member, call) in sorted(members):
        if member in BASE_STATE:
            text = re.sub(r'(?<![\w.])(' + '|'.join(re.escape(v) for v in names) + r')\.' + member + r'(?:\(\))?', r'\g<1>.' + member, text)
            continue
        if member in BUILTIN:
            fld, expr = BUILTIN[member]
            text = re.sub(r'(?<![\w.])(' + '|'.join(re.escape(v) for v in names) + r')\.' + member + r'\(\)', r'\g<1>.' + fld, text)
            continue
        t = member_type(ent, member, call) if ent else None
        if t is None and myrmex:
            t = member_type('EntityMyrmexBase', member, call)
        ecall = call
        if member in KNOWN:
            t = KNOWN[member][0]
            ecall = KNOWN[member][1].endswith('()')
        fld = member
        if t is None:
            todo.append(member)
            t = 'Object /* TODO */'
        fields.append((fld, t, member, ecall))
        for v in names:
            text = re.sub(r'(?<![\w.])' + re.escape(v) + r'\.' + re.escape(member) + (r'\(\)' if call else r'(?!\()'), v + '.' + fld, text)

    # imports
    text = re.sub(r'import com\.google\.common\.collect\.ImmutableList;\n', '', text)
    text = re.sub(r'import [\w.]*BasicModelPart;\n', '', text)
    text = re.sub(r'import com\.nicktale\.api\.animation\.IAnimatedEntity;\n', '', text)
    for im in re.finditer(r'import com\.github\.alexthe666\.iceandfire\.entity\.(Entity\w+);\n', text):
        rest = text.replace(im.group(0), '')
        if not re.search(r'\b' + im.group(1) + r'\b', rest):
            text = rest
    write(path, text)

    # state class
    known_imports = collect_imports(ent)
    needed = []
    for fld, t, member, call in fields:
        for tok in re.findall(r'[A-Z]\w*', t):
            imp = known_imports.get(tok)
            if imp and imp not in needed:
                needed.append(imp)
    state_path = os.path.join(MODEL_DIR, state + '.java')
    all_fields = {f: t for f, t, _, _ in fields}
    if os.path.exists(state_path):
        old = read(state_path)
        for im in re.finditer(r'^import [^\n]+;', old, flags=re.M):
            if im.group(0) not in needed:
                needed.append(im.group(0))
        for fm in re.finditer(r'^    public (.+?) (\w+);', old, flags=re.M):
            all_fields.setdefault(fm.group(2), fm.group(1))
    lines = ['package com.github.alexthe666.iceandfire.client.model;', '']
    lines += needed + ([''] if needed else [])
    lines += ['/** Data the models need from their entity, copied once per frame by the renderer. */',
              'public class %s extends IafRenderState {' % state]
    for f in sorted(all_fields):
        lines.append('    public %s %s;' % (all_fields[f], f))
    lines.append('}')
    lines.append('')
    if (ent or myrmex) and state != 'IafRenderState':
        write(state_path, '\n'.join(lines))
    # extraction snippet
    ex = ['// extractRenderState for %s (%s)' % (name, ent)]
    for fld, t, member, call in fields:
        ex.append('state.%s = entity.%s%s;' % (fld, member, '()' if call else ''))
    print('== %s -> %s  fields=%d  TODO=%s' % (name, state, len(fields), todo))
    print('\n'.join(ex))
    return state


if __name__ == '__main__':
    for n in sys.argv[1:]:
        convert(n)
