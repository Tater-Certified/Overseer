/**
 * Copyright (c) 2024 Dylan Sperrer - dylan@sperrer.ca
 * The project is Licensed under <a href="https://github.com/Tater-Certified/Overseer/blob/dev/LICENSE">MIT</a>
 */
package ca.taterland.tatercertified.overseer.platforms;

import ca.taterland.tatercertified.overseer.Overseer;

import dev.neuralnexus.taterloader.event.api.PluginEvents;

import org.bukkit.plugin.java.JavaPlugin;

/** Bukkit entry point. */
@SuppressWarnings("unused")
public class BukkitPlugin extends JavaPlugin {
    public BukkitPlugin() {
        PluginEvents.ENABLED.register(event -> Overseer.instance().onEnable());
    }

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}
}
