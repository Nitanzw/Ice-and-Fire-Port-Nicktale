"""Adds missing imports for helper/renamed classes that the mass passes introduced. Usage: python tools/ensure_imports.py"""
import os
import re

KNOWN = {
    'IafDamage': 'com.github.alexthe666.iceandfire.util.IafDamage',
    'IafEntityUtil': 'com.github.alexthe666.iceandfire.util.IafEntityUtil',
    'IafDataSerializers': 'com.github.alexthe666.iceandfire.misc.IafDataSerializers',
    'EntityTypes': 'net.minecraft.world.entity.EntityTypes',
    'EntitySpawnReason': 'net.minecraft.world.entity.EntitySpawnReason',
    'ItemStackTemplate': 'net.minecraft.world.item.ItemStackTemplate',
    'NeoForge': 'net.neoforged.neoforge.common.NeoForge',
    'ItemAbilities': 'net.neoforged.neoforge.common.ItemAbilities',
    'CommonHooks': 'net.neoforged.neoforge.common.CommonHooks',
    'EventHooks': 'net.neoforged.neoforge.event.EventHooks',
    'ServerLevel': 'net.minecraft.server.level.ServerLevel',
    'UUIDUtil': 'net.minecraft.core.UUIDUtil',
    'ValueInput': 'net.minecraft.world.level.storage.ValueInput',
    'ValueOutput': 'net.minecraft.world.level.storage.ValueOutput',
    'SynchedEntityData': 'net.minecraft.network.syncher.SynchedEntityData',
    'GameRules': 'net.minecraft.world.level.gamerules.GameRules',
    'BootstrapContext': 'net.minecraft.data.worldgen.BootstrapContext',
    'LivingDamageEvent': 'net.neoforged.neoforge.event.entity.living.LivingDamageEvent',
    'LivingIncomingDamageEvent': 'net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent',
    'FinalizeSpawnEvent': 'net.neoforged.neoforge.event.entity.living.FinalizeSpawnEvent',
    'EntityTickEvent': 'net.neoforged.neoforge.event.tick.EntityTickEvent',
    'EventBusSubscriber': 'net.neoforged.fml.common.EventBusSubscriber',
    'Supplier': 'java.util.function.Supplier',
    'Vec3': 'net.minecraft.world.phys.Vec3',
}

added = 0
for d in ['src/main/java', 'src/client/java']:
    for root, _, fs in os.walk(d):
        for f in fs:
            if not f.endswith('.java'):
                continue
            p = os.path.join(root, f)
            s = open(p, encoding='utf-8').read()
            body = re.sub(r'^import [^\n]*\n', '', s, flags=re.M)
            body = re.sub(r'//[^\n]*|/\*.*?\*/', '', body, flags=re.S)
            pkg = re.search(r'^package ([\w.]+);', s, flags=re.M)
            pkg = pkg.group(1) if pkg else ''
            new = s
            for name, fqn in KNOWN.items():
                if name == 'Supplier':
                    used = re.search(r'\bSupplier<', body)
                elif name in ('ServerLevel', 'Vec3', 'ValueInput', 'ValueOutput', 'SynchedEntityData', 'UUIDUtil'):
                    used = re.search(r'\b%s\b' % name, body)
                else:
                    used = re.search(r'\b%s\b\s*[.<]' % name, body) or re.search(r'\b%s\b' % name, body)
                if not used:
                    continue
                if re.search(r'^import [\w.]*\.%s;' % name, new, flags=re.M):
                    continue
                if fqn.rsplit('.', 1)[0] == pkg:
                    continue
                # class defined in this file?
                if re.search(r'\b(class|interface|enum|record)\s+%s\b' % name, body):
                    continue
                new = re.sub(r'(package [\w.]+;\n)', lambda m: m.group(1) + '\nimport %s;' % fqn, new, count=1)
                added += 1
            if new != s:
                open(p, 'w', encoding='utf-8').write(new)
print('imports added:', added)
