/**
 * Copyright (c) 2024 Dylan Sperrer - dylan@sperrer.ca
 * The project is Licensed under <a href="https://github.com/Tater-Certified/Overseer/blob/dev/LICENSE">MIT</a>
 */
package ca.taterland.tatercertified.overseer.platforms;

import ca.taterland.tatercertified.overseer.Overseer;

import com.google.inject.Inject;

import org.spongepowered.api.plugin.Plugin;

/** Sponge entry point. */
@Plugin(
        id = Overseer.PROJECT_ID,
        name = Overseer.PROJECT_NAME,
        version = Overseer.PROJECT_VERSION,
        description = Overseer.PROJECT_DESCRIPTION)
@SuppressWarnings("unused")
public class Sponge7Plugin {
    @Inject
    public Sponge7Plugin() {
        Overseer.instance().onEnable();
    }
}
