#!/usr/bin/env node

const fs = require('fs');

function createPickaxeTrimRecipe(pickaxe, trim) {
    const recipe = {
        type: 'minecraft:smithing_transform',
        category: 'equipment',
        template: {
            item: 'pickaxetrims:fracture_armor_trim_smithing_template'
        },
        base: {
            item: pickaxe.id
        },
        addition: {
            item: trim.id
        },
        result: {
            item: pickaxe.id
        }
    };
    return recipe;
}

function getRecipeName(pickaxe, trim) {
    const pickaxeName = pickaxe.id.replace('minecraft:', '');
    const trimName = trim.id.replace('minecraft:', '');
    return `fracture_armor_trim_smithing_template_${pickaxeName}_${trimName}_smithing_transform.json`;
}

function writePickaxeTrimRecipe(pickaxe, trim) {
    const recipe = createPickaxeTrimRecipe(pickaxe, trim);
    const recipeName = getRecipeName(pickaxe, trim);
    const recipeString = JSON.stringify(recipe, null, 4) + '\n';
    fs.writeFileSync(recipeName, recipeString, 'utf-8');
}

const pickaxes = [
    { id: 'minecraft:netherite_pickaxe' },
    { id: 'minecraft:diamond_pickaxe' },
    { id: 'minecraft:golden_pickaxe' },
    { id: 'minecraft:iron_pickaxe' },
];

const trims = [
    { id: 'minecraft:crying_obsidian' },
    { id: 'minecraft:lapis_lazuli' },
    { id: 'minecraft:emerald' },
    { id: 'minecraft:quartz' },
    { id: 'minecraft:redstone' },
    { id: 'minecraft:copper_ingot' },
];

for (const pickaxe of pickaxes) {
    for (const trim of trims) {
        writePickaxeTrimRecipe(pickaxe, trim);
    }
}
