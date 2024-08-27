/**
 * Copyright (c) 2024 Dylan Sperrer - dylan@sperrer.ca
 * The project is Licensed under <a href="https://github.com/Tater-Certified/Overseer/blob/dev/LICENSE">MIT</a>
 */
package ca.taterland.tatercertified.overseer.config;

import static dev.neuralnexus.taterapi.util.ConfigUtil.get;
import static dev.neuralnexus.taterapi.util.ConfigUtil.set;

import ca.taterland.tatercertified.overseer.Overseer;
import ca.taterland.tatercertified.overseer.config.sections.DDOSConfig;
import ca.taterland.tatercertified.overseer.config.sections.IPLoggerConfig;
import ca.taterland.tatercertified.overseer.config.versions.OverseerConfig_V1;

import dev.neuralnexus.taterapi.config.MixinConfig;
import dev.neuralnexus.taterapi.config.ToggleableSetting;
import dev.neuralnexus.taterapi.logger.Logger;
import dev.neuralnexus.taterapi.metadata.PlatformData;
import dev.neuralnexus.taterapi.util.ConfigUtil;

import io.leangen.geantyref.TypeToken;

import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/** A class for loading Switchboard configuration. */
public class OverseerConfigLoader {
    private static final Logger logger = Logger.create(Overseer.PROJECT_ID + "-configloader");
    private static final Path configPath =
            Paths.get(
                    PlatformData.instance().configFolder()
                            + File.separator
                            + Overseer.PROJECT_ID
                            + File.separator
                            + Overseer.PROJECT_ID
                            + ".conf");
    private static final String defaultConfigPath = "source." + Overseer.PROJECT_ID + ".conf";
    private static final TypeToken<Integer> versionType = new TypeToken<Integer>() {};
    private static final TypeToken<List<ToggleableSetting>> moduleType =
            new TypeToken<List<ToggleableSetting>>() {};
    private static final TypeToken<MixinConfig> mixinType = new TypeToken<MixinConfig>() {};
    private static final TypeToken<DDOSConfig> ddosType = new TypeToken<DDOSConfig>() {};
    private static final TypeToken<IPLoggerConfig> ipLoggerType =
            new TypeToken<IPLoggerConfig>() {};
    private static OverseerConfig config;

    /** Load the configuration from the file. */
    public static void load() {
        ConfigUtil.copyDefaults(Overseer.class, configPath, defaultConfigPath, logger);

        final HoconConfigurationLoader loader =
                HoconConfigurationLoader.builder().path(configPath).build();
        CommentedConfigurationNode root = ConfigUtil.getRoot(loader, logger);
        if (root == null) {
            return;
        }

        int version = root.node("version").getInt(1);
        List<ToggleableSetting> modules = get(root, moduleType, "modules", logger);
        MixinConfig mixin = get(root, mixinType, "mixin", logger);
        DDOSConfig ddos = get(root, ddosType, "ddos", logger);
        IPLoggerConfig ipLogger = get(root, ipLoggerType, "iplogger", logger);

        switch (version) {
            case 1:
                config = new OverseerConfig_V1(version, modules, mixin, ddos, ipLogger);
                break;
            default:
                logger.error("Unknown configuration version: " + version);
        }
    }

    /** Unload the configuration. */
    public static void unload() {
        config = null;
    }

    /** Save the configuration to the file. */
    public static void save() {
        if (config == null) {
            return;
        }
        final HoconConfigurationLoader loader =
                HoconConfigurationLoader.builder().path(configPath).build();
        CommentedConfigurationNode root = ConfigUtil.getRoot(loader, logger);
        if (root == null) {
            return;
        }

        set(root, versionType, "version", config.version(), logger);
        set(root, moduleType, "modules", config.modules(), logger);
        set(root, mixinType, "mixin", config.mixin(), logger);
        set(root, ddosType, "ddos", config.ddos(), logger);
        set(root, ipLoggerType, "iplogger", config.ipLogger(), logger);

        try {
            loader.save(root);
        } catch (ConfigurateException e) {
            logger.error("An error occurred while saving this configuration: ", e);
        }
    }

    /**
     * Get the loaded configuration.
     *
     * @return The loaded configuration.
     */
    public static OverseerConfig config() {
        if (config == null) {
            load();
        }
        return config;
    }
}
