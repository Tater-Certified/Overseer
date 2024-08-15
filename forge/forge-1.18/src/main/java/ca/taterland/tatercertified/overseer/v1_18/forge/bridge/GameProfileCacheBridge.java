/**
 * Copyright (c) 2024 Dylan Sperrer - dylan@sperrer.ca
 * The project is Licensed under <a href="https://github.com/Tater-Certified/Overseer/blob/dev/LICENSE">MIT</a>
 */
package ca.taterland.tatercertified.overseer.v1_18.forge.bridge;

import ca.taterland.tatercertified.overseer.mixin.v1_18.forge.accessor.GameProfileCacheAccessor;

import net.minecraft.server.players.GameProfileCache;

import java.util.Map;

public interface GameProfileCacheBridge {
    default Map<String, ?> bridge$getProfilesbyName(GameProfileCache cache) {
        return ((GameProfileCacheAccessor) cache).accessor$getProfilesbyName();
    }
}
