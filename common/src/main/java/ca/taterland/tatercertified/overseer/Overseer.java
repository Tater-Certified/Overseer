/**
 * Copyright (c) 2024 Dylan Sperrer - dylan@sperrer.ca
 * The project is Licensed under <a href="https://github.com/Tater-Certified/Overseer/blob/dev/LICENSE">MIT</a>
 */
package ca.taterland.tatercertified.overseer;

import ca.taterland.tatercertified.overseer.api.events.OverseerEvents;
import ca.taterland.tatercertified.overseer.config.OverseerConfig;
import ca.taterland.tatercertified.overseer.config.OverseerConfigLoader;
import ca.taterland.tatercertified.overseer.ddos.ConnectionHandler;
import ca.taterland.tatercertified.overseer.ddos.PlayerNameCache;

import dev.neuralnexus.taterapi.MinecraftVersion;
import dev.neuralnexus.taterapi.Platform;
import dev.neuralnexus.taterapi.TaterAPIProvider;
import dev.neuralnexus.taterapi.event.api.ServerEvents;
import dev.neuralnexus.taterapi.logger.Logger;
import dev.neuralnexus.taterloader.event.api.PluginEvents;

/** Main class for the plugin. */
public class Overseer {
    public static final String PROJECT_NAME = "Overseer";
    public static final String PROJECT_ID = "overseer";
    public static final String PROJECT_VERSION = "0.1.0-SNAPSHOT";
    public static final String PROJECT_AUTHORS = "p0t4t0sandwich";
    public static final String PROJECT_DESCRIPTION = "A cross-platform anticheat mod";
    public static final String PROJECT_URL = "https://github.com/Tater-Certified/Overseer";

    private static final Overseer instance = new Overseer();
    private static final Logger logger = Logger.create(PROJECT_ID);

    public static final PlayerNameCache cache = new PlayerNameCache();
    public static int rateLimit;
    public static int rate = 0;

    public static Logger logger() {
        return logger;
    }

    /**
     * Getter for the singleton instance of the class.
     *
     * @return The singleton instance
     */
    public static Overseer instance() {
        return instance;
    }

    public void onEnable() {
        logger.info(
                Overseer.PROJECT_NAME
                        + " is running on "
                        + Platform.get()
                        + " "
                        + MinecraftVersion.get()
                        + "!");
        PluginEvents.DISABLED.register(event -> onDisable());

        // Config
        OverseerConfigLoader.load();
        OverseerConfig config = OverseerConfigLoader.config();

        // Register API
        //        OverseerAPIProvider.register(new OverseerAPI());

        if (config.checkModule("ddos")) {
            OverseerEvents.HANDLE_HELLO.register(ConnectionHandler::handleHello);

            rateLimit = config.ddos().rateLimit();

            if (config.ddos().useUsercache()) {
                Overseer.cache.addNameSource(
                        () -> TaterAPIProvider.api().get().server().playercache().keySet());
            }
            if (config.ddos().useWhitelist()) {
                Overseer.cache.addNameSource(
                        () -> TaterAPIProvider.api().get().server().whitelist().keySet());
            }
            if (!config.ddos().safeNames().isEmpty()) {
                Overseer.cache.addNameSource(
                        () -> OverseerConfigLoader.config().ddos().safeNames());
            }

            TaterAPIProvider.scheduler()
                    .repeatAsync(
                            () -> {
                                if (Overseer.rate > config.ddos().rateLimit()) {
                                    Overseer.rate = 0;
                                }
                            },
                            0L,
                            config.ddos().rateLimitPeriod() * 20L);
            ServerEvents.STARTED.register(
                    event ->
                            TaterAPIProvider.scheduler()
                                    .repeatAsync(Overseer.cache::refresh, 0L, 20 * 30L));
        }

        logger().info(PROJECT_NAME + " has been started!");
    }

    public void onDisable() {
        // Remove references to objects
        OverseerConfigLoader.unload();

        // Unregister API
        //        OverseerAPIProvider.unregister();

        logger().info(PROJECT_NAME + " has been stopped!");
    }
}
