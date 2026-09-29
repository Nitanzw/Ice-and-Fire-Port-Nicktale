"""Seventh mass pass: removed getMobType, ForgeHooks, isAlliedTo/dropEquipment overrides, enchant helper."""
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


def remove_method(text, header_regex):
    m = re.search(r'(?:[ \t]*@Override[ \t]*\n)?[ \t]*' + header_regex + r'[^{;]*\{', text)
    if not m:
        return text
    end = matching_brace(text, m.end() - 1)
    if end < 0:
        return text
    stop = end + 1
    while stop < len(text) and text[stop] in ' \t':
        stop += 1
    if stop < len(text) and text[stop] == '\n':
        stop += 1
    return text[:m.start()] + text[stop:]


def fix(s):
    while True:
        n = remove_method(s, r'public\s+(?:@NotNull\s+)?MobType\s+getMobType\s*\(\s*\)')
        if n == s:
            break
        s = n
    s = re.sub(r'net\.neoforged\.neoforge\.common\.ForgeHooks\b', 'net.neoforged.neoforge.common.CommonHooks', s)
    s = re.sub(r'\bForgeHooks\b', 'CommonHooks', s)
    # isAlliedTo override -> considersEntityAsAlly (Entity.isAlliedTo is final now)
    s = re.sub(r'public boolean isAlliedTo\((@\w+ )?Entity (\w+)\)', r'public boolean considersEntityAsAlly(\1Entity \2)', s)
    # super.isAlliedTo(x) inside those overrides
    if 'considersEntityAsAlly(' in s:
        s = re.sub(r'super\.isAlliedTo\((\w+)\)', r'super.considersEntityAsAlly(\1)', s)
    # dropEquipment override
    if re.search(r'void dropEquipment\(\)', s):
        s = re.sub(r'(protected|public) void dropEquipment\(\)', r'\1 void dropEquipment(ServerLevel level)', s)
        s = re.sub(r'super\.dropEquipment\(\)', 'super.dropEquipment(level)', s)
        if 'import net.minecraft.server.level.ServerLevel;' not in s:
            s = re.sub(r'(package [\w.]+;\n)', r'\1\nimport net.minecraft.server.level.ServerLevel;', s, count=1)
    s = re.sub(r'EnchantmentHelper\.hasVanishingCurse\((\w+)\)',
               r'EnchantmentHelper.has(\1, net.minecraft.world.item.enchantment.EnchantmentEffectComponents.PREVENT_EQUIPMENT_DROP)', s)
    return s


changed = 0
for d in dirs:
    for root, _, fs in os.walk(d):
        for f in fs:
            if not f.endswith('.java'):
                continue
            p = os.path.join(root, f)
            s = open(p, encoding='utf-8').read()
            n = fix(s)
            if n != s:
                open(p, 'w', encoding='utf-8').write(n)
                changed += 1
print('files changed:', changed)
