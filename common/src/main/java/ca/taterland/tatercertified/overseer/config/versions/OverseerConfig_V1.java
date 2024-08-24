/**
 * Copyright (c) 2024 Dylan Sperrer - dylan@sperrer.ca
 * The project is Licensed under <a href="https://github.com/Tater-Certified/Overseer/blob/dev/LICENSE">MIT</a>
 */
package ca.taterland.tatercertified.overseer.config.versions;

import ca.taterland.tatercertified.overseer.config.OverseerConfig;
import ca.taterland.tatercertified.overseer.config.sections.DDOSConfig;

import dev.neuralnexus.taterapi.config.MixinConfig;
import dev.neuralnexus.taterapi.config.ToggleableSetting;

import java.util.List;

/** A class for Overseer configuration. */
public class OverseerConfig_V1 implements OverseerConfig {
    private final int version;
    private final List<ToggleableSetting> modules;
    private final MixinConfig mixin;
    private final DDOSConfig ddos;

    public OverseerConfig_V1(
            int version, List<ToggleableSetting> modules, MixinConfig mixin, DDOSConfig ddos) {
        this.version = version;
        this.modules = modules;
        this.mixin = mixin;
        this.ddos = ddos;
    }

    @Override
    public int version() {
        return this.version;
    }

    @Override
    public List<ToggleableSetting> modules() {
        return this.modules;
    }

    @Override
    public MixinConfig mixin() {
        return this.mixin;
    }

    @Override
    public DDOSConfig ddos() {
        return this.ddos;
    }
}
