/**
 * Copyright (c) 2024 Dylan Sperrer - dylan@sperrer.ca
 * The project is Licensed under <a href="https://github.com/Tater-Certified/Overseer/blob/dev/LICENSE">MIT</a>
 */
package ca.taterland.tatercertified.overseer;

import ca.taterland.tatercertified.overseer.api.OverseerAPI;
import ca.taterland.tatercertified.overseer.api.events.OverseerEvents;
import ca.taterland.tatercertified.overseer.config.OverseerConfig;
import ca.taterland.tatercertified.overseer.config.OverseerConfigLoader;
import ca.taterland.tatercertified.overseer.ddos.DDOS;
import ca.taterland.tatercertified.overseer.iplogger.IPLogger;

import dev.neuralnexus.taterapi.MinecraftVersion;
import dev.neuralnexus.taterapi.Platform;
import dev.neuralnexus.taterapi.TaterAPIProvider;
import dev.neuralnexus.taterapi.event.api.ServerEvents;
import dev.neuralnexus.taterapi.logger.Logger;
import dev.neuralnexus.taterloader.event.api.PluginEvents;
import dev.neuralnexus.taterloader.plugin.Plugin;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Overseer implements Plugin {
    public static final String PROJECT_NAME = "Overseer";
    public static final String PROJECT_ID = "overseer";
    public static final String PROJECT_VERSION = "0.1.0-SNAPSHOT";
    public static final String PROJECT_AUTHORS = "p0t4t0sandwich";
    public static final String PROJECT_DESCRIPTION = "A cross-platform anticheat mod";
    public static final String PROJECT_URL = "https://github.com/Tater-Certified/Overseer";

    private static final Overseer instance = new Overseer();
    private static final Logger logger = Logger.create(PROJECT_ID);

    public static Overseer instance() {
        return instance;
    }

    public static Logger logger() {
        return logger;
    }

    @Override
    public String name() {
        return Overseer.PROJECT_NAME;
    }

    @Override
    public String id() {
        return Overseer.PROJECT_ID;
    }

    @Override
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
        OverseerAPI.register(new OverseerAPI());

        // Setup for DDOS protection
        if (config.checkModule("ddos")) {
            OverseerEvents.HANDLE_HELLO.register(DDOS::handleHello);

            DDOS.rateLimit = config.ddos().rateLimit();

            if (config.ddos().useUsercache()) {
                OverseerAPI.get()
                        .ddos()
                        .addNameSource(
                                () -> TaterAPIProvider.api().get().server().playercache().keySet());
            }
            if (config.ddos().useWhitelist()) {
                OverseerAPI.get()
                        .ddos()
                        .addNameSource(
                                () -> TaterAPIProvider.api().get().server().whitelist().keySet());
            }
            if (!config.ddos().safeNames().isEmpty()) {
                OverseerAPI.get()
                        .ddos()
                        .addNameSource(() -> OverseerConfigLoader.config().ddos().safeNames());
            }

            TaterAPIProvider.scheduler()
                    .repeatAsync(
                            () -> {
                                if (DDOS.rate > config.ddos().rateLimit()) {
                                    DDOS.rate = 0;
                                }
                            },
                            0L,
                            config.ddos().rateLimitPeriod() * 20L);
            ServerEvents.STARTED.register(
                    event ->
                            TaterAPIProvider.scheduler()
                                    .repeatAsync(OverseerAPI.get().ddos()::refresh, 0L, 20 * 30L));
        }

        // Setup for IP logging
        if (config.checkModule("iplogger")) {
            if (config.ipLogger().ddos()) {
                DDOS.logIps = true;
            }
            String timestamp =
                    new SimpleDateFormat("yyyyMMdd_HHmmss")
                            .format(Calendar.getInstance().getTime());
            IPLogger.logFile =
                    new File(
                            config.ipLogger().folder()
                                    + File.separator
                                    + "ips-"
                                    + timestamp
                                    + ".csv");
            if (!IPLogger.logFile.exists()) {
                try {
                    IPLogger.logFile.createNewFile();
                } catch (Exception e) {
                    logger.error("Failed to create the IP log file!", e);
                }
            }
            TaterAPIProvider.scheduler().repeatAsync(IPLogger::flush, 0L, 20 * 180L);
            OverseerEvents.LOG_IP.register(IPLogger::logIp);
        }

        logger.info(PROJECT_NAME + " has been started!");
    }

    @Override
    public void onDisable() {
        // Flush any pending IP logs
        if (OverseerConfigLoader.config().checkModule("iplogger")) {
            IPLogger.flush();
        }

        // Remove references to objects
        OverseerConfigLoader.unload();

        // Unregister API
        OverseerAPI.unregister();

        logger.info(PROJECT_NAME + " has been stopped!");
    }
}
