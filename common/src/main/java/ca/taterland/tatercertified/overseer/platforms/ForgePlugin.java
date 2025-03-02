/**
 * Copyright (c) 2024 Dylan Sperrer - dylan@sperrer.ca
 * The project is Licensed under <a href="https://github.com/Tater-Certified/Overseer/blob/dev/LICENSE">MIT</a>
 */
package ca.taterland.tatercertified.overseer.platforms;

import ca.taterland.tatercertified.overseer.Overseer;

import dev.neuralnexus.taterapi.meta.enums.MinecraftVersion;
import dev.neuralnexus.taterapi.util.ReflectionUtil;
import dev.neuralnexus.taterloader.event.api.PluginEvents;

import net.minecraftforge.fml.common.Mod;

/** Forge entry point. */
@Mod(
        value = Overseer.PROJECT_ID,
        modid = Overseer.PROJECT_ID,
        useMetadata = true,
        serverSideOnly = true,
        acceptableRemoteVersions = "*")
@SuppressWarnings("unused")
public class ForgePlugin {
    public ForgePlugin() {
        PluginEvents.ENABLED.register(event -> Overseer.instance().onEnable());
        MinecraftVersion mcv = MinecraftVersion.get();
        String classStr;
        if (mcv.isInRange(MinecraftVersion.V14, MinecraftVersion.V16_5)) {
            classStr = "ca.taterland.tatercertified.overseer.v1_14_4.forge.OverseerForge";
        } else if (mcv.isInRange(MinecraftVersion.V17, MinecraftVersion.V17_1)) {
            classStr = "ca.taterland.tatercertified.overseer.v1_17_1.forge.OverseerForge";
        } else if (mcv.isInRange(MinecraftVersion.V18, MinecraftVersion.V19_3)) {
            classStr = "ca.taterland.tatercertified.overseer.v1_19.forge.OverseerForge";
        } else {
            classStr = "ca.taterland.tatercertified.overseer.v1_19_4.forge.OverseerForge";
        }
        ReflectionUtil.newInstance(classStr);
    }
}
