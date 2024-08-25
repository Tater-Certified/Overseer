/**
 * Copyright (c) 2024 Dylan Sperrer - dylan@sperrer.ca
 * The project is Licensed under <a href="https://github.com/Tater-Certified/Overseer/blob/dev/LICENSE">MIT</a>
 */
package ca.taterland.tatercertified.overseer.platforms;

import ca.taterland.tatercertified.overseer.Overseer;

import dev.neuralnexus.taterloader.event.api.PluginEvents;

import net.md_5.bungee.api.plugin.Plugin;

/** Bungee entry point. */
@SuppressWarnings("unused")
public class BungeePlugin extends Plugin {
    public BungeePlugin() {
        PluginEvents.ENABLED.register(event -> Overseer.instance().onEnable());
    }
}
