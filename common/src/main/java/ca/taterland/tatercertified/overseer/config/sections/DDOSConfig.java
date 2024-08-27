/**
 * Copyright (c) 2024 Dylan Sperrer - dylan@sperrer.ca
 * The project is Licensed under <a href="https://github.com/Tater-Certified/Overseer/blob/dev/LICENSE">MIT</a>
 */
package ca.taterland.tatercertified.overseer.config.sections;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.List;

/** Config section for DDOS related settings */
@ConfigSerializable
public class DDOSConfig {
    @Setting private int rateLimit;
    @Setting private int rateLimitPeriod;
    @Setting private boolean useUsercache;
    @Setting private boolean useWhitelist;
    @Setting private List<String> safeNames;
    @Setting private List<String> blacklistedNames;
    @Setting private boolean trashTalkEnabled;
    @Setting private List<String> trashTalk;

    public int rateLimit() {
        return this.rateLimit;
    }

    public int rateLimitPeriod() {
        return this.rateLimitPeriod;
    }

    public boolean useUsercache() {
        return this.useUsercache;
    }

    public boolean useWhitelist() {
        return this.useWhitelist;
    }

    public List<String> safeNames() {
        return this.safeNames;
    }

    public List<String> blacklistedNames() {
        return this.blacklistedNames;
    }

    public boolean trashTalkEnabled() {
        return this.trashTalkEnabled;
    }

    public List<String> trashTalk() {
        return this.trashTalk;
    }
}
