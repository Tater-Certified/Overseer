/**
 * Copyright (c) 2024 Dylan Sperrer - dylan@sperrer.ca
 * The project is Licensed under <a href="https://github.com/Tater-Certified/Overseer/blob/dev/LICENSE">MIT</a>
 */
package ca.taterland.tatercertified.overseer.platforms;

import ca.taterland.tatercertified.overseer.Overseer;

import net.neoforged.fml.common.Mod;

/** NeoForge entry point. */
@Mod(Overseer.PROJECT_ID)
public class NeoForgePlugin {
    public NeoForgePlugin() {
        Overseer.instance().onEnable();
    }
}
