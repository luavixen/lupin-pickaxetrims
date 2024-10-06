package dev.foxgirl.pickaxetrims.shared;

import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public interface PickaxeTrimsRegistryInterface {

    <T> Supplier<@NotNull T> registerItem(@NotNull Identifier id, @NotNull Supplier<T> supplier);
    <T> Supplier<@NotNull T> registerDataComponentType(@NotNull Identifier id, @NotNull Supplier<T> supplier);

}
