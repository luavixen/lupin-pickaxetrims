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
        writePickaxeTrimRecipe(pickaxe, trim);
    }
}

function createPickaxeTrimRecipe(pickaxe, trim) {
    const recipe = {
        type: 'minecraft:smithing_transform',
        template: 'pickaxetrims:fracture_armor_trim_smithing_template',
        base: pickaxe.id,
        addition: trim.id,
        result: {
            id: pickaxe.id,
            components: {
                'pickaxetrims:trim': { ingredient: trim.id },
                'minecraft:item_model': `pickaxetrims:placeholder_${pickaxe.name}_trimmed_${trim.name}`
            }
        }
    };
    return recipe;
}

function getRecipeName(pickaxe, trim) {
    return `fracture_armor_trim_smithing_template_${pickaxe.name}_${trim.name}_smithing_transform.json`;
}

function writePickaxeTrimRecipe(pickaxe, trim) {
    const recipe = createPickaxeTrimRecipe(pickaxe, trim);
    const recipeName = getRecipeName(pickaxe, trim);
    const recipeString = JSON.stringify(recipe, null, 4) + '\n';
    fs.writeFileSync(recipeName, recipeString, 'utf-8');
}
