package dev.foxgirl.pickaxetrims;

import dev.foxgirl.pickaxetrims.shared.PickaxeTrimsImpl;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@Mod("pickaxetrims")
public class PickaxeTrimsMod {

    private final PickaxeTrimsImpl impl = new PickaxeTrimsImpl();

    public PickaxeTrimsMod(IEventBus modEventBus) {
        modEventBus.addListener(this::onCommonSetup);
        modEventBus.addListener(this::onBuildCreativeModeTabContents);

        impl.initialize();
    }

    private void onCommonSetup(FMLCommonSetupEvent event) {
    }

    private void onBuildCreativeModeTabContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == ItemGroups.INGREDIENTS) {
            event.insertAfter(
                new ItemStack(Items.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE),
                new ItemStack(impl.getSmithingTemplateItem()),
                ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS
            );
        } else if (event.getTabKey() == impl.getItemGroupKey()) {
            impl.forEachStackForItemGroup(event::add);
        }
    }

}
