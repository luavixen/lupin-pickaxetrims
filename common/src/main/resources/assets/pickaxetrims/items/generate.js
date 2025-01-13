#!/usr/bin/env node

const fs = require('fs');

const pickaxes = [
    { id: 'minecraft:netherite_pickaxe', name: 'netherite_pickaxe' },
    { id: 'minecraft:diamond_pickaxe', name: 'diamond_pickaxe' },
    { id: 'minecraft:golden_pickaxe', name: 'gold_pickaxe' },
    { id: 'minecraft:iron_pickaxe', name: 'iron_pickaxe' },
];

const trims = [
    { id: 'minecraft:crying_obsidian', name: 'crying_obsidian' },
    { id: 'minecraft:lapis_lazuli', name: 'lapis_lazuli' },
    { id: 'minecraft:emerald', name: 'emerald' },
    { id: 'minecraft:quartz', name: 'quartz' },
    { id: 'minecraft:redstone', name: 'redstone' },
    { id: 'minecraft:copper_ingot', name: 'copper' },
];

for (const pickaxe of pickaxes) {
    for (const trim of trims) {
        writePlacholderPickaxeItem(pickaxe, trim);
    }
}

function createPlaceholderPickaxeItem(pickaxe, trim) {
    const item = {
        model: {
            type: 'minecraft:model',
            model: `pickaxetrims:item/${pickaxe.name}_trimmed_${trim.name}`
        }
    };
    return item;
}

function writePlacholderPickaxeItem(pickaxe, trim) {
    const item = createPlaceholderPickaxeItem(pickaxe, trim);
    const itemName = `placeholder_${pickaxe.name}_trimmed_${trim.name}.json`;
    const itemString = JSON.stringify(item, null, 4) + '\n';
    fs.writeFileSync(itemName, itemString, 'utf-8');
}
