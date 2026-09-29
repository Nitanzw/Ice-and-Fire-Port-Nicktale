"""Third mass pass: synched entity data builders, NBT -> ValueInput/ValueOutput scalars, small API renames."""
import os
import re
import sys

dirs = [a for a in sys.argv[1:] if not a.startswith('--')] or ['src/main/java', 'src/client/java']


def matching_brace(text, open_idx):
    depth = 0
    for j in range(open_idx, len(text)):
        c = text[j]
        if c == '{':
            depth += 1
        elif c == '}':
            depth -= 1
            if depth == 0:
                return j
    return -1


# ---------- 1. SynchedEntityData builders ----------
def fix_synched(s):
    def repl(m):
        head = m.group(0)
        return head
    out = s
    # method header: protected void defineSynchedData() {
    for m in list(re.finditer(r'(protected|public) void defineSynchedData\(\)\s*\{', out)):
        pass
    pos = 0
    res = ''
    for m in re.finditer(r'(protected|public) void defineSynchedData\(\)\s*\{', out):
        if m.start() < pos:
            continue
        end = matching_brace(out, m.end() - 1)
        if end < 0:
            continue
        body = out[m.end():end]
        body = re.sub(r'\b(?:this\.)?entityData\.define\(', 'builder.define(', body)
        body = re.sub(r'super\.defineSynchedData\(\)', 'super.defineSynchedData(builder)', body)
        res += out[pos:m.start()] + '%s void defineSynchedData(SynchedEntityData.Builder builder) {' % m.group(1) + body
        pos = end
    res += out[pos:]
    if res != out and 'import net.minecraft.network.syncher.SynchedEntityData;' not in res:
        res = re.sub(r'(package [\w.]+;\n)', r'\1\nimport net.minecraft.network.syncher.SynchedEntityData;', res, count=1)
    return res


# ---------- 2. NBT scalars in save/load methods ----------
INPUT_DEFAULTS = {
    'Boolean': ('getBooleanOr', 'false'),
    'Int': ('getIntOr', '0'),
    'Float': ('getFloatOr', '0.0F'),
    'Double': ('getDoubleOr', '0.0D'),
    'Long': ('getLongOr', '0L'),
    'String': ('getStringOr', '""'),
    'Byte': ('getByteOr', '(byte) 0'),
    'Short': ('getShortOr', '(short) 0'),
}


def fix_nbt(s):
    # signatures
    s = re.sub(r'void addAdditionalSaveData\(CompoundTag (\w+)\)', r'void addAdditionalSaveData(ValueOutput \1)', s)
    s = re.sub(r'void readAdditionalSaveData\(CompoundTag (\w+)\)', r'void readAdditionalSaveData(ValueInput \1)', s)
    out = ''
    pos = 0
    for m in re.finditer(r'(?:public|protected|private)?[^\n;{}]*\((?:[^)]*?)\b(ValueInput|ValueOutput)\s+(\w+)(?:[^)]*)\)[^;{]*\{', s):
        if m.start() < pos:
            continue
        kind, var = m.group(1), m.group(2)
        end = matching_brace(s, m.end() - 1)
        if end < 0:
            continue
        body = s[m.end():end]
        v = re.escape(var)
        if kind == 'ValueInput':
            for t, (fn, dflt) in INPUT_DEFAULTS.items():
                body = re.sub(r'\b%s\.get%s\(("[^"]*"|\w+)\)' % (v, t), r'%s.%s(\1, %s)' % (var, fn, dflt), body)
            body = re.sub(r'\b%s\.hasUUID\(("[^"]*"|\w+)\)' % v, r'%s.read(\1, UUIDUtil.CODEC).isPresent()' % var, body)
            body = re.sub(r'\b%s\.getUUID\(("[^"]*"|\w+)\)' % v, r'%s.read(\1, UUIDUtil.CODEC).orElse(null)' % var, body)
        else:
            body = re.sub(r'\b%s\.putUUID\(("[^"]*"|\w+),\s*([^;]*?)\);' % v, r'%s.store(\1, UUIDUtil.CODEC, \2);' % var, body)
        out += s[pos:m.end()] + body
        pos = end
    out += s[pos:]
    if out != s:
        for imp, name in (('net.minecraft.world.level.storage.ValueInput', 'ValueInput'),
                          ('net.minecraft.world.level.storage.ValueOutput', 'ValueOutput'),
                          ('net.minecraft.core.UUIDUtil', 'UUIDUtil')):
            if re.search(r'\b%s\b' % name, out) and ('import %s;' % imp) not in out:
                out = re.sub(r'(package [\w.]+;\n)', r'\1\nimport %s;' % imp, out, count=1)
    return out


# ---------- 3. small renames ----------
SIMPLE = [
    (r'\.displayClientMessage\(([^;]*?),\s*true\)', r'.sendOverlayMessage(\1)'),
    (r'\.displayClientMessage\(([^;]*?),\s*false\)', r'.sendSystemMessage(\1)'),
    (r'\bMobEffects\.DAMAGE_BOOST\b', 'MobEffects.STRENGTH'),
    (r'\bMobEffects\.MOVEMENT_SLOWDOWN\b', 'MobEffects.SLOWNESS'),
    (r'\bMobEffects\.MOVEMENT_SPEED\b', 'MobEffects.SPEED'),
    (r'\bMobEffects\.DIG_SLOWDOWN\b', 'MobEffects.MINING_FATIGUE'),
    (r'\bMobEffects\.DIG_SPEED\b', 'MobEffects.HASTE'),
    (r'\bMobEffects\.HEAL\b', 'MobEffects.INSTANT_HEALTH'),
    (r'\bMobEffects\.HARM\b', 'MobEffects.INSTANT_DAMAGE'),
    (r'\bMobEffects\.JUMP\b', 'MobEffects.JUMP_BOOST'),
    (r'\bMobEffects\.CONFUSION\b', 'MobEffects.NAUSEA'),
    (r'\bMobEffects\.DAMAGE_RESISTANCE\b', 'MobEffects.RESISTANCE'),
    (r'\.setSecondsOnFire\(', '.igniteForSeconds('),
    (r'Minecraft\.getInstance\(\)\.getFrameTime\(\)', 'Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(false)'),
    (r'((?:\w+(?:\(\))?\.)*\w+(?:\(\))?)\.getMinBuildHeight\(\)', r'\1.getMinY()'),
    (r'((?:\w+(?:\(\))?\.)*\w+(?:\(\))?)\.getMaxBuildHeight\(\)', r'(\1.getMaxY() + 1)'),
    (r'\bBlocks\.GRASS\b(?!_)', 'Blocks.SHORT_GRASS'),
    (r'\.isDay\(\)', '.isBrightOutside()'),
]


def fix_simple(s):
    for pat, rep in SIMPLE:
        s = re.sub(pat, rep, s)
    return s


changed = 0
for d in dirs:
    for root, _, fs in os.walk(d):
        for f in fs:
            if not f.endswith('.java'):
                continue
            p = os.path.join(root, f)
            s = open(p, encoding='utf-8').read()
            o = s
            s = fix_synched(s)
            s = fix_nbt(s)
            s = fix_simple(s)
            if s != o:
                open(p, 'w', encoding='utf-8').write(s)
                changed += 1
print('files changed:', changed)
