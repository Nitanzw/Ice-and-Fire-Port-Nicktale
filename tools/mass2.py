"""Second mass pass: well-known renames between 1.20 Forge and 26.x NeoForge (whole-word text replacements)."""
import os
import re
import sys

dirs = [a for a in sys.argv[1:] if not a.startswith('--')] or ['src/main/java', 'src/client/java']

# (regex, replacement)
RULES = [
    (r'net\.minecraft\.data\.worldgen\.BootstapContext', 'net.minecraft.data.worldgen.BootstrapContext'),
    (r'\bBootstapContext\b', 'BootstrapContext'),
    (r'net\.neoforged\.neoforge\.common\.MinecraftForge', 'net.neoforged.neoforge.common.NeoForge'),
    (r'\bMinecraftForge\.EVENT_BUS\b', 'NeoForge.EVENT_BUS'),
    (r'\bMinecraftForge\b', 'NeoForge'),
    (r'net\.neoforged\.neoforge\.common\.ToolActions', 'net.neoforged.neoforge.common.ItemAbilities'),
    (r'\bToolActions\b', 'ItemAbilities'),
    (r'net\.minecraft\.world\.entity\.MobSpawnType', 'net.minecraft.world.entity.EntitySpawnReason'),
    (r'\bMobSpawnType\b', 'EntitySpawnReason'),
    (r'net\.neoforged\.neoforge\.event\.ForgeEventFactory', 'net.neoforged.neoforge.event.EventHooks'),
    (r'\bForgeEventFactory\b', 'EventHooks'),
]

changed = 0
for d in dirs:
    for root, _, fs in os.walk(d):
        for f in fs:
            if not f.endswith('.java'):
                continue
            p = os.path.join(root, f)
            s = open(p, encoding='utf-8').read()
            o = s
            for pat, rep in RULES:
                s = re.sub(pat, rep, s)
            if s != o:
                open(p, 'w', encoding='utf-8').write(s)
                changed += 1
print('files changed:', changed)
