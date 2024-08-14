package dev.neuralnexus.tatercertified.overseer;

import com.google.common.collect.Sets;

import dev.neuralnexus.tatercertified.overseer.bridge.GameProfileCacheBridge;
import dev.neuralnexus.tatercertified.overseer.bridge.StoredUserEntryBridge;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.players.UserWhiteListEntry;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.Map;
import java.util.Set;

public class PlayerListCache implements GameProfileCacheBridge, StoredUserEntryBridge {
    private static final Set<String> usercache = Sets.newConcurrentHashSet();
    private static final Set<String> whitelist = Sets.newConcurrentHashSet();

    PlayerListCache() {}

    public void update() {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();

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
