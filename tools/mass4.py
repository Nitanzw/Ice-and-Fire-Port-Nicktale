"""Fourth mass pass: EntityType constants moved to EntityTypes; misc verified renames."""
import os
import re
import sys

dirs = [a for a in sys.argv[1:] if not a.startswith('--')] or ['src/main/java', 'src/client/java']
changed = 0
for d in dirs:
    for root, _, fs in os.walk(d):
        for f in fs:
            if not f.endswith('.java'):
                continue
            p = os.path.join(root, f)
            s = open(p, encoding='utf-8').read()
            o = s
            # EntityType.PIG -> EntityTypes.PIG (constants are ALL_CAPS; keep EntityType.Builder etc.)
            s = re.sub(r'\bEntityType\.([A-Z][A-Z0-9_]{2,})\b(?!\()', r'EntityTypes.\1', s)
            if s != o and 'import net.minecraft.world.entity.EntityTypes;' not in s and 'package net.minecraft.world.entity;' not in s:
                s = re.sub(r'(package [\w.]+;\n)', r'\1\nimport net.minecraft.world.entity.EntityTypes;', s, count=1)
            if s != o:
                open(p, 'w', encoding='utf-8').write(s)
                changed += 1
print('files changed:', changed)
