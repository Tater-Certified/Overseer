/**
 * Copyright (c) 2024 Dylan Sperrer - dylan@sperrer.ca
 * The project is Licensed under <a href="https://github.com/Tater-Certified/Overseer/blob/dev/LICENSE">MIT</a>
 */
package ca.taterland.tatercertified.overseer;

import ca.taterland.tatercertified.overseer.ddos.PlayerListCache;
import dev.neuralnexus.taterapi.MinecraftVersion;
import dev.neuralnexus.taterapi.Platform;
import dev.neuralnexus.taterapi.logger.Logger;

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

    public static final PlayerListCache cache = new PlayerListCache();
    public static int rateLimit = 0;
    public static boolean superAttackMode = false;

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

        // Config
        //        OverseerConfigLoader.load();

        // Register API
        //        OverseerAPIProvider.register(new OverseerAPI());

        logger().info(PROJECT_NAME + " has been started!");
    }

    public void onDisable() {
        // Remove references to objects
        //        OverseerConfigLoader.unload();

        // Unregister API
        //        OverseerAPIProvider.unregister();

        logger().info(PROJECT_NAME + " has been stopped!");
    }
}
