"""One-off conversion of the humanoid models (Ghost, Dread*) to BipedRenderState."""
import os
import re

M = 'src/main/java/com/github/alexthe666/iceandfire/client/model'


def rd(n):
    with open(os.path.join(M, n + '.java'), encoding='utf-8') as f:
        return f.read()


def wr(n, s):
    with open(os.path.join(M, n + '.java'), 'w', encoding='utf-8') as f:
        f.write(s)


def remove_method(text, header_regex):
    m = re.search(r'(?:[ \t]*@Override\s*\n)?[ \t]*' + header_regex + r'[^{;]*\{', text)
    if not m:
        return text
    i = m.end()
    depth = 1
    while depth and i < len(text):
        if text[i] == '{':
            depth += 1
        elif text[i] == '}':
            depth -= 1
        i += 1
    end = i
    while end < len(text) and text[end] in ' \t':
        end += 1
    if end < len(text) and text[end] == '\n':
        end += 1
    return text[:m.start()] + text[end:]


def common(text, ent):
    text = text.replace('<%s>' % ent, '<BipedRenderState>')
    for h in [r'public\s+void\s+prepareMobModel\s*\(', r'public\s+void\s+setLivingAnimations\s*\(',
              r'public\s+void\s+copyPropertiesTo\s*\(', r'public\s+Iterable<\w+>\s+getAllParts\s*\(',
              r'public\s+Iterable<\w+>\s+parts\s*\(']:
        text = remove_method(text, h)
    text = text.replace('animator.update(entity);', 'animator.update(entity.animation, entity.animationTick);')
    return text


# ---- Ghost
t = common(rd('ModelGhost'), 'EntityGhost')
t = t.replace('public void setupAnim(EntityGhost entity, float f, float f1, float f2, float f3, float f4) {',
              'protected void animate(BipedRenderState entity) {\n'
              '        float f = entity.walkAnimationPos;\n        float f1 = entity.walkAnimationSpeed;\n'
              '        float f2 = entity.ageInTicks;\n        float f3 = entity.yRot;\n        float f4 = entity.xRot;')
t = t.replace('public void animate(EntityGhost entity,', 'public void animate(BipedRenderState entity,')
wr('ModelGhost', t)

# ---- DreadGhoul
t = common(rd('ModelDreadGhoul'), 'EntityDreadGhoul')
t = t.replace('public void setupAnim(EntityDreadGhoul thrall, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {',
              'protected void animate(BipedRenderState thrall) {\n'
              '        float limbSwing = thrall.walkAnimationPos;\n        float limbSwingAmount = thrall.walkAnimationSpeed;\n'
              '        float ageInTicks = thrall.ageInTicks;\n        float netHeadYaw = thrall.yRot;\n        float headPitch = thrall.xRot;')
t = t.replace('thrall.getAnimation()', 'thrall.animation').replace('thrall.getAnimationTick()', 'thrall.animationTick')
t = t.replace('void animate(EntityDreadGhoul entity,', 'void animate(BipedRenderState entity,')
wr('ModelDreadGhoul', t)

# ---- Knight
t = common(rd('ModelDreadKnight'), 'EntityDreadKnight')
t = t.replace('public void setRotationAnglesSpawn(EntityDreadKnight entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {',
              'public void setRotationAnglesSpawn(BipedRenderState entityIn) {')
t = t.replace('public void animate(EntityDreadKnight entity,', 'public void animate(BipedRenderState entity,')
wr('ModelDreadKnight', t)

# ---- Lich
t = common(rd('ModelDreadLich'), 'EntityDreadLich')
t = t.replace(' implements ArmedModel {', ' {')
t = t.replace('public void setupAnim(EntityDreadLich entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {\n'
              '        super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);',
              'protected void animate(BipedRenderState entityIn) {\n'
              '        super.animate(entityIn);\n        float ageInTicks = entityIn.ageInTicks;')
t = t.replace('entityIn.getAnimation()', 'entityIn.animation')
wr('ModelDreadLich', t)

# ---- Queen
wr('ModelDreadQueen', common(rd('ModelDreadQueen'), 'EntityDreadQueen'))

# ---- Thrall
t = common(rd('ModelDreadThrall'), 'EntityDreadThrall')
t = t.replace('public void setupAnim(EntityDreadThrall entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {\n'
              '        super.setupAnim(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);\n'
              '        this.flap(body, 0.5F, 0.15F, false, 1, 0F, limbSwing, limbSwingAmount);',
              'protected void animate(BipedRenderState entityIn) {\n'
              '        super.animate(entityIn);\n'
              '        this.flap(body, 0.5F, 0.15F, false, 1, 0F, entityIn.walkAnimationPos, entityIn.walkAnimationSpeed);')
wr('ModelDreadThrall', t)
print('done')
