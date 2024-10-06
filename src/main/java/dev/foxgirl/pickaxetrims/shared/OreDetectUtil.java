package dev.foxgirl.pickaxetrims.shared;

import net.minecraft.block.Block;
import net.minecraft.registry.Registries;

import java.util.regex.Pattern;

public final class OreDetectUtil {

    private static final Pattern ORE_ID_PATTERN = Pattern.compile("(^|\\W|_)ore(_|\\W|$)");

    public static boolean isOreBlock(Block block) {
        var path = Registries.BLOCK.getId(block).getPath();
        var matched = ORE_ID_PATTERN.matcher(path).find();
        return matched;
    }

}
