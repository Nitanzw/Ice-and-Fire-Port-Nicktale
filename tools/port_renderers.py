"""Converts simple MobRenderer subclasses to IafMobRenderer (render-state pipeline).

Each entry: renderer -> (entity class, model class, state class, extraction lines).
Layers and special overrides (light level, render(), shouldRender) are ported by hand afterwards.
"""
import os
import re

R = 'src/main/java/com/github/alexthe666/iceandfire/client/render/entity'

JOBS = {
    'RenderPixie': ('EntityPixie', 'ModelPixie', 'PixieRenderState',
                    ['state.isPixieSitting = entity.isPixieSitting();',
                     'state.heldItem = entity.getItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND);']),
    'RenderTroll': ('EntityTroll', 'ModelTroll', 'TrollRenderState',
                    ['state.stoneProgress = entity.stoneProgress;']),
    'RenderGorgon': ('EntityGorgon', 'ModelGorgon', 'GorgonRenderState', []),
    'RenderStymphalianBird': ('EntityStymphalianBird', 'ModelStymphalianBird', 'StymphalianBirdRenderState',
                              ['state.flyProgress = entity.flyProgress;']),
    'RenderCyclops': ('EntityCyclops', 'ModelCyclops', 'CyclopsRenderState', []),
    'RenderAmphithere': ('EntityAmphithere', 'ModelAmphithere', 'AmphithereRenderState',
                         ['state.diveProgress = entity.diveProgress;', 'state.flapProgress = entity.flapProgress;',
                          'state.groundProgress = entity.groundProgress;', 'state.onGround = entity.onGround();',
                          'state.pitch_buffer = entity.pitch_buffer;', 'state.roll_buffer = entity.roll_buffer;',
                          'state.sitProgress = entity.sitProgress;', 'state.tail_buffer = entity.tail_buffer;']),
    'RenderSiren': ('EntitySiren', 'ModelSiren', 'SirenRenderState',
                    ['state.getSingingPose = entity.getSingingPose();', 'state.isSinging = entity.isSinging();',
                     'state.isSwimming = entity.isSwimming();', 'state.onGround = entity.onGround();',
                     'state.singProgress = entity.singProgress;', 'state.swimProgress = entity.swimProgress;',
                     'state.tail_buffer = entity.tail_buffer;']),
    'RenderDeathWorm': ('EntityDeathWorm', 'ModelDeathWorm', 'DeathWormRenderState',
                        ['state.getWormJumping = entity.getWormJumping();', 'state.jumpProgress = entity.jumpProgress;',
                         'state.prevJumpProgress = entity.prevJumpProgress;', 'state.tail_buffer = entity.tail_buffer;',
                         'state.tickCount = entity.tickCount;']),
    'RenderHippocampus': ('EntityHippocampus', 'ModelHippocampus', 'HippocampusRenderState',
                          ['state.onGround = entity.onGround();', 'state.onLandProgress = entity.onLandProgress;',
                           'state.sitProgress = entity.sitProgress;', 'state.tail_buffer = entity.tail_buffer;']),
    'RenderHydra': ('EntityHydra', 'ModelHydraBody', 'HydraRenderState', []),
}


def rd(p):
    with open(p, encoding='utf-8') as f:
        return f.read()


def wr(p, s):
    with open(p, 'w', encoding='utf-8') as f:
        f.write(s)


for name, (ent, model, state, lines) in JOBS.items():
    p = os.path.join(R, name + '.java')
    t = rd(p)
    t = re.sub(r'extends MobRenderer<%s, %s>' % (ent, model),
               'extends IafMobRenderer<%s, %s, %s>' % (ent, state, model), t)
    t = t.replace('this.layers.add(', 'this.addLayer(')
    # texture
    t = re.sub(r'(?:@Nullable\s*\n\s*)?(?:@Override\s*\n\s*)?public (?:@NotNull )?Identifier getTextureLocation\((?:@NotNull )?%s (\w+)\)' % ent,
               r'@Override\n    protected Identifier textureFor(%s \1)' % ent, t)
    # scale
    t = re.sub(r'@Override\s*\n\s*(?:public|protected) void scale\((?:@NotNull )?%s (\w+), (?:@NotNull )?PoseStack (\w+), float (\w+)\)' % ent,
               r'@Override\n    protected void scaleFor(%s \1, PoseStack \2, float \3)' % ent, t)
    # extraction
    body = ''.join('        %s\n' % l for l in lines)
    extra = ('\n    @Override\n    protected %s createRenderState() {\n        return new %s();\n    }\n' % (state, state))
    if lines:
        extra += ('\n    @Override\n    protected void extract(%s entity, %s state, float partialTick) {\n%s    }\n' % (ent, state, body))
    # insert before the first override
    idx = t.index('    @Override')
    t = t[:idx].rstrip('\n') + '\n' + extra + '\n' + t[idx:]
    # imports
    imp = ('import com.github.alexthe666.iceandfire.client.model.%s;\n' % state)
    t = t.replace('\nimport ', '\n' + imp + 'import ', 1)
    wr(p, t)
    print('ported', name)
