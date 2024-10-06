package dev.foxgirl.pickaxetrims.shared;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import dev.architectury.platform.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

public final class PickaxeTrimsConfig {

    public int cryingObsidianMultiBreakRadius = 1;

    public int lapisGlowRadius = 10;
    public float lapisGlowSeconds = 10.0F;

    public int redstoneVeinMineDepth = 16;

    public float copperDurabilityMultiplier = 2.0F;

    public float emeraldDoubleDropsChance = 0.5F;

    public float quartzExperienceMultiplierMin = 1.5F;
    public float quartzExperienceMultiplierMax = 3.0F;

    public float smithingTemplateLootProbability = 0.1F;
    public List<String> smithingTemplateLootTables = List.of(
        "minecraft:chests/desert_pyramid",
        "minecraft:chests/buried_treasure",
        "minecraft:chests/shipwreck_treasure",
        "minecraft:chests/stronghold_corridor",
        "minecraft:chests/stronghold_crossing",
        "minecraft:chests/stronghold_library",
        "minecraft:chests/woodland_mansion",
        "minecraft:chests/village/village_toolsmith"
    );

    public static PickaxeTrimsConfig loadConfig() {
        Logger logger = LogManager.getLogger();

        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();

        Path configPath = Platform.getConfigFolder().resolve("pickaxetrims.json");
        Path tempPath = Platform.getConfigFolder().resolve("pickaxetrims.json.tmp");

        try {
            return gson.fromJson(Files.newBufferedReader(configPath), PickaxeTrimsConfig.class);
        } catch (NoSuchFileException cause) {
            logger.warn("Config file not found, will be created");
        } catch (JsonParseException cause) {
            logger.error("Failed to parse config file", cause);
        } catch (Exception cause) {
            logger.error("Failed to load config file", cause);
        }

        PickaxeTrimsConfig config = new PickaxeTrimsConfig();

        try {
            Files.writeString(tempPath, gson.toJson(config, PickaxeTrimsConfig.class));
            Files.move(tempPath, configPath, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception cause) {
            logger.error("Failed to save new config file", cause);
        }

        return config;
    }

}
