package dev.foxgirl.pickaxetrims;

import dev.architectury.platform.forge.EventBuses;
import dev.foxgirl.pickaxetrims.shared.PickaxeTrimsImpl;
import net.minecraft.item.ItemGroups;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("pickaxetrims")
public class PickaxeTrimsMod {

    private final PickaxeTrimsImpl impl = new PickaxeTrimsImpl();

    public PickaxeTrimsMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::onCommonSetup);
        modEventBus.addListener(this::onBuildCreativeModeTabContents);

        EventBuses.registerModEventBus("pickaxetrims", modEventBus);

        impl.initialize();
    }

    private void onCommonSetup(FMLCommonSetupEvent event) {
    }

    private void onBuildCreativeModeTabContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == ItemGroups.INGREDIENTS) {
            event.add(impl.getSmithingTemplateItem());
        } else if (event.getTabKey() == impl.getItemGroupKey()) {
            impl.forEachStackForItemGroup(event::add);
        }
    }

}
