/**
 * Copyright (c) 2024 Dylan Sperrer - dylan@sperrer.ca
 * The project is Licensed under <a href="https://github.com/Tater-Certified/Overseer/blob/dev/LICENSE">MIT</a>
 */
package ca.taterland.tatercertified.overseer.config.sections;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

/** Config section for DDOS related settings */
@ConfigSerializable
public class IPLoggerConfig {
    @Setting private String folder;
    @Setting private boolean ddos;

    public String folder() {
        return this.folder;
    }

    public boolean ddos() {
        return this.ddos;
    }
}
