/**
 * Copyright (c) 2024 Dylan Sperrer - dylan@sperrer.ca
 * The project is Licensed under <a href="https://github.com/Tater-Certified/Overseer/blob/dev/LICENSE">MIT</a>
 */
package ca.taterland.tatercertified.overseer.platforms;

import ca.taterland.tatercertified.overseer.Overseer;

import com.google.inject.Inject;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;

import dev.neuralnexus.taterloader.event.api.PluginEvents;

/** Velocity entry point. */
@Plugin(
        id = Overseer.PROJECT_ID,
        name = Overseer.PROJECT_NAME,
        version = Overseer.PROJECT_VERSION,
        authors = Overseer.PROJECT_AUTHORS,
        description = Overseer.PROJECT_DESCRIPTION,
        url = Overseer.PROJECT_URL,
        dependencies = {@Dependency(id = "taterlib")})
@SuppressWarnings("unused")
public class VelocityPlugin {
    @Inject
    public VelocityPlugin() {
        PluginEvents.ENABLED.register(event -> Overseer.instance().onEnable());
    }
}
