#!/usr/bin/env python3
"""Builds the Minecraft 26.3 resource overlay (src/mc/26.3/resources) from the 26.2 data.

26.3 changed several data formats. Only files that actually change are written to the overlay, which the 26.3 build puts
first so they replace the 26.2 copies:
  * worldgen/configured_feature (type + config) -> worldgen/feature (type + flattened fields)
  * block states {"Name", "Properties"} -> "id" or {"id", "properties"}
  * loot tables: number providers need a "type" ({"min", "max"} -> minecraft:uniform)
  * advancements: recipe_unlocked "recipe" -> "recipes"; entity predicates are one condition object ("type"),
    several -> minecraft:all_of
  * cooking recipes: "cookingtime" is required
  * brewing mixes are data recipes
Run after datagen:  python tools/gen_mc263_resources.py
"""
import json
import shutil
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
OUT = ROOT / 'src/mc/26.3/resources/data'
BASES = [ROOT / 'src/main/resources/data', ROOT / 'src/generated/resources/data']
COOKING_TIME = {'minecraft:smelting': 200, 'minecraft:blasting': 100, 'minecraft:smoking': 100,
                'minecraft:campfire_cooking': 600}
NUMBER_KEYS = {'rolls', 'bonus_rolls', 'count', 'levels', 'value', 'damage'}


def convert_state(value):
    """26.2 {"Name", "Properties"} block state -> 26.3 "id" string or {"id", "properties"}."""
    if isinstance(value, dict) and 'Name' in value:
        props = value.get('Properties')
        return {'id': value['Name'], 'properties': props} if props else value['Name']
    return value


def block_states(node):
    if isinstance(node, dict):
        if set(node) <= {'type', 'state'} and node.get('type') == 'minecraft:simple_state_provider':
            state = convert_state(node['state'])
            return {'id': state} if isinstance(state, str) else state
        if 'Name' in node and set(node) <= {'Name', 'Properties'}:
            return convert_state(node)
        return {k: block_states(v) for k, v in node.items()}
    if isinstance(node, list):
        return [block_states(v) for v in node]
    return node


def number_providers(node, key=None):
    """Loot tables: untyped {"min", "max"} number providers become minecraft:uniform."""
    if isinstance(node, dict):
        if key in NUMBER_KEYS and 'type' not in node and set(node) == {'min', 'max'}:
            return {'type': 'minecraft:uniform', 'min': number_providers(node['min'], 'value'),
                    'max': number_providers(node['max'], 'value')}
        return {k: number_providers(v, k) for k, v in node.items()}
    if isinstance(node, list):
        return [number_providers(v, key) for v in node]
    return node


def loot_condition(cond):
    if isinstance(cond, dict) and 'condition' in cond and 'type' not in cond:
        cond = {('type' if k == 'condition' else k): v for k, v in cond.items()}
    return cond


def advancement(node):
    criteria = node.get('criteria') if isinstance(node, dict) else None
    if not isinstance(criteria, dict):
        return node
    for criterion in criteria.values():
        conditions = criterion.get('conditions')
        if not isinstance(conditions, dict):
            continue
        if criterion.get('trigger') == 'minecraft:recipe_unlocked' and 'recipe' in conditions:
            conditions['recipes'] = conditions.pop('recipe')
        for k, v in list(conditions.items()):
            if isinstance(v, list) and v and all(isinstance(c, dict) and ('condition' in c or 'type' in c) for c in v):
                terms = [loot_condition(c) for c in v]
                conditions[k] = terms[0] if len(terms) == 1 else {'type': 'minecraft:all_of', 'terms': terms}
    return node


def recipe(node):
    if isinstance(node, dict) and node.get('type') in COOKING_TIME and 'cookingtime' not in node:
        node = dict(node)
        node['cookingtime'] = COOKING_TIME[node['type']]
    return node


def write(path: Path, value) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(value, indent=2) + '\n', encoding='utf-8')


def main() -> None:
    shutil.rmtree(ROOT / 'src/mc/26.3/resources', ignore_errors=True)
    features = converted = 0
    seen = set()
    for base in BASES:
        if not base.is_dir():
            continue
        for f in sorted(base.rglob('*.json')):
            rel = f.relative_to(base)
            if rel in seen:
                continue          # first copy wins, same order as the 26.2 jar (main, then generated)
            seen.add(rel)
            parts = rel.parts     # namespace / folder / ...
            old = json.loads(f.read_text(encoding='utf-8'))
            if parts[1:3] == ('worldgen', 'configured_feature'):
                new = {'type': old['type']}
                new.update(block_states(old.get('config') or {}))
                write(OUT / parts[0] / 'worldgen/feature' / Path(*parts[3:]), new)
                features += 1
                continue
            new = block_states(json.loads(json.dumps(old)))
            if parts[1] == 'loot_table':
                new = number_providers(new)
            elif parts[1] == 'advancement':
                new = advancement(new)
            elif parts[1] == 'recipe':
                new = recipe(new)
            if new != old:
                write(OUT / rel, new)
                converted += 1
    # brewing: water potion + shiny scales -> water breathing, for every potion container
    for item in ('potion', 'splash_potion', 'lingering_potion'):
        write(OUT / f'iceandfire/recipe/brewing/{item}_water_breathing_from_shiny_scales.json', {
            'type': 'minecraft:brewing',
            'input': {'item': f'minecraft:{item}', 'potion_contents': {'potions': 'minecraft:water'}},
            'output': {'id': f'minecraft:{item}', 'components': {'minecraft:potion_contents': {'potion': 'minecraft:water_breathing'}}},
            'reagent': {'item': 'iceandfire:shiny_scales'},
        })
    print(f'{features} features, {converted} converted data files, 3 brewing recipes -> {OUT}')


if __name__ == '__main__':
    main()
