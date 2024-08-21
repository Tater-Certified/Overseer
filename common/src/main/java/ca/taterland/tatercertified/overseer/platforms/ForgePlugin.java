/**
 * Copyright (c) 2024 Dylan Sperrer - dylan@sperrer.ca
 * The project is Licensed under <a href="https://github.com/Tater-Certified/Overseer/blob/dev/LICENSE">MIT</a>
 */
package ca.taterland.tatercertified.overseer.platforms;

import ca.taterland.tatercertified.overseer.Overseer;
import ca.taterland.tatercertified.overseer.util.ReflectionUtil;

import dev.neuralnexus.taterapi.MinecraftVersion;

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
        Overseer.instance().onEnable();
        MinecraftVersion mcv = MinecraftVersion.get();
        String classStr;
        if (mcv.isInRange(MinecraftVersion.V1_14, MinecraftVersion.V1_16_5)) {
            classStr = "ca.taterland.tatercertified.overseer.v1_14_4.forge.OverseerForge";
        } else if (mcv.isInRange(MinecraftVersion.V1_17, MinecraftVersion.V1_17_1)) {
            classStr = "ca.taterland.tatercertified.overseer.v1_17_1.forge.OverseerForge";
        } else if (mcv.isInRange(MinecraftVersion.V1_18, MinecraftVersion.V1_19_3)) {
            classStr = "ca.taterland.tatercertified.overseer.v1_19.forge.OverseerForge";
        } else {
            classStr = "ca.taterland.tatercertified.overseer.v1_19_4.forge.OverseerForge";
        }
        ReflectionUtil.newInstance(classStr);
    }
}
