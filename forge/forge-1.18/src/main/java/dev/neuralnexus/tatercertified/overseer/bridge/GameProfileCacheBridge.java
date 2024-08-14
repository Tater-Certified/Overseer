package dev.neuralnexus.tatercertified.overseer.bridge;

import dev.neuralnexus.tatercertified.overseer.mixin.accessor.GameProfileCacheAccessor;
import net.minecraft.server.players.GameProfileCache;

import java.util.Map;

public interface GameProfileCacheBridge {
    default Map<String, ?> bridge$getProfilesbyName(GameProfileCache cache) {
        return ((GameProfileCacheAccessor) cache).accessor$getProfilesbyName();
    }
}
