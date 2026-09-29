"""One-off conversion of the static / statue / item models to the render-state API."""
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


def strip_common(text):
    for h in [r'public\s+void\s+renderStatue\s*\(', r'public\s+Iterable<\w+>\s+getAllParts\s*\(',
              r'public\s+(?:@NotNull\s+)?Iterable<\w+>\s+parts\s*\(']:
        text = remove_method(text, h)
    text = re.sub(r'import com\.google\.common\.collect\.ImmutableList;\n', '', text)
    text = re.sub(r'import [\w.]*BasicModelPart;\n', '', text)
    return text


STATE_IMPORT = 'import net.minecraft.client.renderer.entity.state.EntityRenderState;\n'


def simple(name, header_regex, generic='EntityRenderState'):
    t = strip_common(rd(name))
    t = re.sub(header_regex, 'extends AdvancedEntityModel<%s>' % generic, t, count=1)
    t = re.sub(r'public void setupAnim\(\s*[\w<>@ ]+?\s+\w+\s*,\s*float \w+\s*,\s*float \w+\s*,\s*float \w+\s*,\s*float \w+\s*,\s*float \w+\s*\)',
               'protected void animate(%s state)' % generic, t)
    t = t.replace('@Override\n    public void setRotateAngle(AdvancedModelBox', '@Override\n    public void setRotateAngle(AdvancedModelBox')
    if 'EntityRenderState' in t and STATE_IMPORT not in t:
        t = t.replace('\nimport ', '\n' + STATE_IMPORT + 'import ', 1)
    wr(name, t)


for n in ['ModelCube', 'ModelDreadLichSkull']:
    simple(n, r'extends AdvancedEntityModel\b(?!<)')
for n in ['ModelTideTrident', 'ModelTrollWeapon', 'ModelGorgonHead', 'ModelGorgonHeadActive']:
    simple(n, r'extends AdvancedEntityModel<Entity>')
simple('ModelPixieHouse', r'extends AdvancedEntityModel<LivingEntity>')

# ---- Dragon egg: the renderer decides whether the egg is shaking
t = strip_common(rd('ModelDragonEgg'))
t = t.replace('public class ModelDragonEgg<T extends LivingEntity> extends AdvancedEntityModel<T> {',
              'public class ModelDragonEgg extends AdvancedEntityModel<DragonEggRenderState> {')
i = t.index('    @Override\n    public void setupAnim(LivingEntity entity')
j = t.index('    public void renderPodium()')
t = (t[:i] +
     '    @Override\n    protected void animate(DragonEggRenderState state) {\n'
     '        float f2 = state.ageInTicks;\n'
     '        this.resetToDefaultPose();\n'
     '        this.Egg1.setPos(0.0F, 19.6F, 0.0F);\n'
     '        this.Egg4.setPos(0.0F, -0.9F, 0.0F);\n'
     '        if (state.shaking) {\n'
     '            this.walk(Egg1, 0.3F, 0.3F, true, 1, 0, f2, 1);\n'
     '            this.flap(Egg1, 0.3F, 0.3F, false, 0, 0, f2, 1);\n'
     '        }\n'
     '    }\n\n' + t[j:])
for imp in ['import com.github.alexthe666.iceandfire.entity.DragonType;\n', 'import com.github.alexthe666.iceandfire.entity.EntityDragonEgg;\n',
            'import net.minecraft.world.entity.LivingEntity;\n']:
    t = t.replace(imp, '')
wr('ModelDragonEgg', t)
wr('DragonEggRenderState', 'package com.github.alexthe666.iceandfire.client.model;\n\n'
   '/** Whether the egg is in its element (burning block for fire eggs, rain for lightning eggs) is decided by the renderer. */\n'
   'public class DragonEggRenderState extends IafRenderState {\n    public boolean shaking;\n}\n')

# ---- Guardian statue
t = strip_common(rd('ModelGuardianStatue'))
t = t.replace('extends AdvancedEntityModel<Entity>', 'extends AdvancedEntityModel<GuardianStatueRenderState>')
t = re.sub(r'public void setupAnim\(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch\) \{\n'
           r'\s*Guardian entityguardian = \(Guardian\) entityIn;\n\s*float f = ageInTicks - \(float\) entityguardian\.tickCount;\n',
           'protected void animate(GuardianStatueRenderState state) {\n'
           '        float ageInTicks = state.ageInTicks;\n        float netHeadYaw = state.yRot;\n        float headPitch = state.xRot;\n', t)
t = re.sub(r'        Entity entity = Minecraft\.getInstance\(\)\.getCameraEntity\(\);\n\n        if \(entityguardian\.hasActiveAttackTarget\(\)\) \{\n            entity = entityguardian\.getActiveAttackTarget\(\);\n        \}\n\n', '', t)
t = t.replace('float f2 = entityguardian.getTailAnimation(f);', 'float f2 = state.tailAnimation;')
t = t.replace('import net.minecraft.world.entity.monster.Guardian;\n', '').replace('import net.minecraft.client.Minecraft;\n', '')
wr('ModelGuardianStatue', t)
wr('GuardianStatueRenderState', 'package com.github.alexthe666.iceandfire.client.model;\n\n'
   '/** Tail animation phase of the guardian a statue was made from. */\n'
   'public class GuardianStatueRenderState extends IafRenderState {\n    public float tailAnimation;\n}\n')

# ---- Chain tie knot
wr('ModelChainTie', '''package com.github.alexthe666.iceandfire.client.model;

import com.nicktale.api.client.model.AdvancedEntityModel;
import com.nicktale.api.client.model.AdvancedModelBox;
import net.minecraft.client.renderer.entity.state.EntityRenderState;

public class ModelChainTie extends AdvancedEntityModel<EntityRenderState> {
    public AdvancedModelBox knotRenderer;

    public ModelChainTie() {
        this(0, 0, 32, 32);
    }

    public ModelChainTie(int width, int height, int texWidth, int texHeight) {
        this.texWidth = texWidth;
        this.texHeight = texHeight;
        this.knotRenderer = new AdvancedModelBox(this, width, height);
        this.knotRenderer.addBox(-4.0F, 2.0F, -4.0F, 8, 12, 8, 1.0F);
        this.knotRenderer.setPos(0.0F, 0.0F, 0.0F);
        this.updateDefaultPose();
    }

    @Override
    protected void animate(EntityRenderState state) {
    }
}
''')

# ---- Banner
wr('ModelBanner', '''package com.github.alexthe666.iceandfire.client.model;

import com.nicktale.api.client.model.AdvancedEntityModel;
import com.nicktale.api.client.model.AdvancedModelBox;
import net.minecraft.client.renderer.entity.state.EntityRenderState;

public class ModelBanner extends AdvancedEntityModel<EntityRenderState> {
    public final AdvancedModelBox flag;
    public final AdvancedModelBox pole;
    public final AdvancedModelBox bar;

    public ModelBanner() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.flag = new AdvancedModelBox(this, 0, 0);
        this.flag.addBox(-10.0F, 0.0F, -2.0F, 20.0F, 40.0F, 1.0F, 0.0F);
        this.pole = new AdvancedModelBox(this, 44, 0);
        this.pole.addBox(-1.0F, -30.0F, -1.0F, 2.0F, 42.0F, 2.0F, 0.0F);
        this.bar = new AdvancedModelBox(this, 0, 42);
        this.bar.addBox(-10.0F, -32.0F, -1.0F, 20.0F, 2.0F, 2.0F, 0.0F);
        this.updateDefaultPose();
    }

    @Override
    protected void animate(EntityRenderState state) {
    }
}
''')

# ---- Death worm gauntlet (item model): the item renderer passes the lunge ticks
t = strip_common(rd('ModelDeathWormGauntlet'))
t = t.replace('extends ModelDragonBase {', 'extends ModelDragonBase<EntityRenderState> {')
i = t.index('    @Override\n    public void setupAnim(Entity entityIn')
j = t.index('    public void animate(ItemStack stack, float partialTick) {')
k = t.index('        /*animator.setAnimation')
t = (t[:i] + '    @Override\n    protected void animate(EntityRenderState state) {\n    }\n\n'
     '    /** Poses the gauntlet; lungeTicks (with partial tick) comes from the holder\'s extra entity data. */\n'
     '    public void animate(float lungeTicks) {\n        this.resetToDefaultPose();\n'
     '        progressRotation(TopJaw, lungeTicks, (float) Math.toRadians(-30), 0, 0);\n'
     '        progressRotation(BottomJaw, lungeTicks, (float) Math.toRadians(30), 0, 0);\n'
     '        progressPosition(JawExtender, lungeTicks, 0, 0, -4);\n'
     '        progressPosition(JawExtender2, lungeTicks, 0, 0, -10);\n'
     '        progressPosition(JawExtender3, lungeTicks, 0, 0, -10);\n'
     '        progressPosition(JawExtender4, lungeTicks, 0, 0, -10);\n    }\n}\n')
for imp in ['import com.github.alexthe666.iceandfire.entity.props.EntityDataProvider;\n', 'import com.mojang.blaze3d.vertex.PoseStack;\n',
            'import com.mojang.blaze3d.vertex.VertexConsumer;\n', 'import net.minecraft.client.Minecraft;\n',
            'import net.minecraft.client.renderer.texture.OverlayTexture;\n', 'import net.minecraft.nbt.CompoundTag;\n',
            'import net.minecraft.world.entity.Entity;\n', 'import net.minecraft.world.entity.LivingEntity;\n',
            'import net.minecraft.world.item.ItemStack;\n']:
    t = t.replace(imp, '')
t = t.replace('\nimport ', '\n' + STATE_IMPORT + 'import ', 1)
wr('ModelDeathWormGauntlet', t)

# ---- Vanilla based statue models
wr('ModelHorseStatue', '''package com.github.alexthe666.iceandfire.client.model;

import net.minecraft.client.model.HorseModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.EquineRenderState;

public class ModelHorseStatue extends HorseModel {
    public ModelHorseStatue(ModelPart part) {
        super(part);
    }

    @Override
    public void setupAnim(EquineRenderState state) {
    }
}
''')
wr('ModelStonePlayer', '''package com.github.alexthe666.iceandfire.client.model;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;

public class ModelStonePlayer extends HumanoidModel<HumanoidRenderState> {
    public ModelStonePlayer(ModelPart root) {
        super(root);
    }

    @Override
    public void setupAnim(HumanoidRenderState state) {
    }
}
''')
print('done')
