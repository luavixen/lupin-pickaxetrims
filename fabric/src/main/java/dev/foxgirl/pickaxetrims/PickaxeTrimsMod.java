package dev.foxgirl.pickaxetrims;

import dev.foxgirl.pickaxetrims.shared.PickaxeTrimsImpl;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;

public class PickaxeTrimsMod implements ModInitializer {

    private final PickaxeTrimsImpl impl = new PickaxeTrimsImpl();

    @Override
    public void onInitialize() {
        impl.initialize();

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(content -> {
            content.addAfter(Items.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE, impl.getSmithingTemplateItem());
        });
        ItemGroupEvents.modifyEntriesEvent(impl.getItemGroupKey()).register(content -> {
            impl.forEachStackForItemGroup(content::add);
        });
    }

}
