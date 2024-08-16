/**
 * Copyright (c) 2024 Dylan Sperrer - dylan@sperrer.ca
 * The project is Licensed under <a href="https://github.com/Tater-Certified/Overseer/blob/dev/LICENSE">MIT</a>
 */
package ca.taterland.tatercertified.overseer.v1_18.forge;

import ca.taterland.tatercertified.overseer.v1_18.forge.bridge.GameProfileCacheBridge;
import ca.taterland.tatercertified.overseer.v1_18.forge.bridge.StoredUserEntryBridge;

import com.google.common.collect.Sets;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.players.UserWhiteListEntry;

import java.util.Map;
import java.util.Set;

public class PlayerListCache implements GameProfileCacheBridge, StoredUserEntryBridge {
    private static final Set<String> usercache = Sets.newConcurrentHashSet();
    private static final Set<String> whitelist = Sets.newConcurrentHashSet();

    PlayerListCache() {}

    public void update(MinecraftServer server) {
        Map<String, ?> cache = this.bridge$getProfilesbyName(server.getProfileCache());
        usercache.clear();
        usercache.addAll(cache.keySet());

        whitelist.clear();
        for (UserWhiteListEntry user : server.getPlayerList().getWhiteList().getEntries()) {
            whitelist.add(this.bridge$getUser(user).getName());
        }
    }

    public static boolean checkName(String name) {
        return usercache.contains(name) || whitelist.contains(name);
    }
}
