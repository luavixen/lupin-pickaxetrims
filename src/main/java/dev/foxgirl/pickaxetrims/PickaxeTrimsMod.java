package dev.foxgirl.pickaxetrims;

import dev.foxgirl.pickaxetrims.shared.PickaxeTrimsImpl;
import dev.foxgirl.pickaxetrims.shared.PickaxeTrimsRegistryInterface;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.ComponentType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class PickaxeTrimsMod implements ModInitializer {

    private final PickaxeTrimsImpl impl = new PickaxeTrimsImpl(new PickaxeTrimsRegistryInterface() {

        @Override @SuppressWarnings("unchecked")
        public <T> Supplier<@NotNull T> registerItem(@NotNull Identifier id, @NotNull Supplier<T> supplier) {
            var value = Registry.register(Registries.ITEM, id, (Item) supplier.get());
            return () -> (T) value;
        }

        @Override @SuppressWarnings("unchecked")
        public <T> Supplier<@NotNull T> registerDataComponentType(@NotNull Identifier id, @NotNull Supplier<T> supplier) {
            var value = Registry.register(Registries.DATA_COMPONENT_TYPE, id, (ComponentType<?>) supplier.get());
            return () -> (T) value;
        }

    });

    @Override
    public void onInitialize() {
        impl.initialize();

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(content -> {
            content.addAfter(Items.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE, impl.getSmithingTemplateItem());
        });
    }

}
