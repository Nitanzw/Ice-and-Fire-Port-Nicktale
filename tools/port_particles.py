"""Converts the custom-render TextureSheetParticle classes to SingleQuadParticle (26.x)."""
import os
import re

P = 'src/main/java/com/github/alexthe666/iceandfire/client/particle'

FILES = ['ParticleBlood', 'ParticleDragonFlame', 'ParticleDragonFrost', 'ParticleDreadPortal',
         'ParticleDreadTorch', 'ParticleHydraBreath', 'ParticlePixieDust', 'ParticleSerpentBubble',
         'ParticleSirenMusic']


def rd(p):
    with open(p, encoding='utf-8') as f:
        return f.read()


def wr(p, s):
    with open(p, 'w', encoding='utf-8') as f:
        f.write(s)


def method_span(text, header_regex):
    m = re.search(header_regex, text)
    if not m:
        return None
    i = text.index('{', m.end() - 1)
    depth = 0
    j = i
    while j < len(text):
        if text[j] == '{':
            depth += 1
        elif text[j] == '}':
            depth -= 1
            if depth == 0:
                break
        j += 1
    start = m.start()
    # include preceding @Override line
    pre = text.rfind('\n', 0, start)
    line_before = text[text.rfind('\n', 0, pre) + 1:pre].strip() if pre > 0 else ''
    if line_before.startswith('@Override'):
        start = text.rfind('\n', 0, pre) + 1
    return start, j + 1


for name in FILES:
    p = os.path.join(P, name + '.java')
    t = rd(p)
    if 'TextureSheetParticle' not in t:
        continue
    # texture names used by the old render
    textures = re.findall(r'Identifier\.parse\("iceandfire:textures/particles/(\w+)\.png"\)', t)
    tex_const = re.findall(r'private static final Identifier (\w+) = Identifier\.parse\("iceandfire:textures/particles/(\w+)\.png"\);', t)
    first = tex_const[0][1] if tex_const else (textures[0] if textures else 'blood')
    # 1. class header
    t = t.replace('extends TextureSheetParticle', 'extends SingleQuadParticle')
    # 2. super call with sprite
    t = re.sub(r'super\(([^;]*?)\);', lambda m: 'super(%s, IafParticleSprites.get("%s"));' % (m.group(1), first)
               if m.group(1).count(',') >= 3 else m.group(0), t, count=1)
    # 3. render() -> per-frame update called from getQuadSize
    span = method_span(t, r'public void render\(')
    prelude = ''
    if span:
        body = t[span[0]:span[1]]
        cut = body.find('Vec3 Vector3d = renderInfo.getPosition();')
        start = body.index('{') + 1
        prelude = body[start:cut] if cut > 0 else ''
        prelude = prelude.replace('Vec3 inerp = renderInfo.getPosition();', '')
        prelude = re.sub(r'\n\s*//TODO[^\n]*', '', prelude)
        t = t[:span[0]] + t[span[1]:]
        # big texture selection
        big_expr = None
        m = re.search(r'RenderSystem\.setShaderTexture\(0, (.+?)\);', body)
        if m:
            big_expr = m.group(1)
        new = ('\n    @Override\n    public float getQuadSize(float partialTicks) {\n'
               '        updateFrame(partialTicks);\n        return super.getQuadSize(partialTicks);\n    }\n\n'
               '    private void updateFrame(float partialTicks) {%s    }\n' % prelude)
        idx = t.rindex('}')
        t = t[:idx].rstrip() + '\n' + new + '}\n'
        # sprite for "big ? A : B" style selection
        if big_expr and '?' in big_expr:
            cond, rest = big_expr.split('?', 1)
            a, b = [x.strip() for x in rest.split(':')]
            names = dict((c, n) for c, n in tex_const)
            if a in names and b in names:
                t = t.replace('big = random.nextBoolean();',
                              'big = random.nextBoolean();\n        this.sprite = IafParticleSprites.get(big ? "%s" : "%s");' % (names[a], names[b]), 1)
    # 4. light and layer
    t = t.replace('public int getLightColor(float partialTick)', 'public int getLightCoords(float partialTick)')
    t = t.replace('return super.getLightColor(partialTick);', 'return super.getLightCoords(partialTick);')
    span = method_span(t, r'public @NotNull ParticleRenderType getRenderType\(\)')
    if span:
        t = t[:span[0]] + t[span[1]:]
    span = method_span(t, r'public int getFXLayer\(\)')
    if span:
        t = t[:span[0]] + t[span[1]:]
    layer = ('\n    @Override\n    protected SingleQuadParticle.Layer getLayer() {\n'
             '        return SingleQuadParticle.Layer.TRANSLUCENT;\n    }\n')
    idx = t.rindex('}')
    t = t[:idx].rstrip() + '\n' + layer + '}\n'
    t = t.replace('\nimport ', '\nimport net.minecraft.client.particle.SingleQuadParticle;\nimport ', 1)
    # remove now-unused static texture constants
    t = re.sub(r'\n\s*private static final Identifier \w+ = Identifier\.parse\("iceandfire:textures/particles/\w+\.png"\);', '', t)
    wr(p, t)
    print('ported', name, first)
