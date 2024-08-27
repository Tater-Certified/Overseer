/**
 * Copyright (c) 2024 Dylan Sperrer - dylan@sperrer.ca
 * The project is Licensed under <a href="https://github.com/Tater-Certified/Overseer/blob/dev/LICENSE">MIT</a>
 */
package ca.taterland.tatercertified.overseer.config;

import ca.taterland.tatercertified.overseer.config.sections.DDOSConfig;
import ca.taterland.tatercertified.overseer.config.sections.IPLoggerConfig;

import dev.neuralnexus.taterapi.config.MixinConfig;
import dev.neuralnexus.taterapi.config.ToggleableSetting;

import java.util.List;

/** A class for Overseer configuration */
public interface OverseerConfig {
    /**
     * Get the version of the configuration
     *
     * @return The version of the configuration
     */
    int version();

    /**
     * Get the modules in the configuration
     *
     * @return The modules in the configuration
     */
    List<ToggleableSetting> modules();

    /**
     * Get the mixins in the configuration
     *
     * @return The mixins in the configuration
     */
    MixinConfig mixin();

    /**
     * Get the DDOS config
     *
     * @return The DDOS config
     */
    DDOSConfig ddos();

    /**
     * Get the IP Logger config
     *
     * @return The IP Logger config
     */
    IPLoggerConfig ipLogger();

    /**
     * Check if a module is enabled in the configuration.
     *
     * @param moduleName The name of the module.
     * @return Whether the module should be applied.
     */
    default boolean checkModule(String moduleName) {
        return modules().stream()
                .anyMatch(
                        moduleConfig ->
                                moduleConfig.name().equals(moduleName) && moduleConfig.enabled());
    }
}
