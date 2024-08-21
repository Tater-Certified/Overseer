/**
 * Copyright (c) 2024 Dylan Sperrer - dylan@sperrer.ca
 * The project is Licensed under <a href="https://github.com/Tater-Certified/Overseer/blob/dev/LICENSE">MIT</a>
 */
package ca.taterland.tatercertified.overseer.platforms;

import ca.taterland.tatercertified.overseer.Overseer;

import com.google.inject.Inject;

import org.spongepowered.plugin.builtin.jvm.Plugin;

/** Sponge entry point. */
@Plugin(Overseer.PROJECT_ID)
@SuppressWarnings("unused")
public class Sponge8Plugin {
    @Inject
    public Sponge8Plugin() {
        Overseer.instance().onEnable();
    }
}
