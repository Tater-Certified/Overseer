/**
 * Copyright (c) 2024 Dylan Sperrer - dylan@sperrer.ca
 * The project is Licensed under <a href="https://github.com/Tater-Certified/Overseer/blob/dev/LICENSE">MIT</a>
 */
package ca.taterland.tatercertified.overseer.v1_18.vanilla.bridge;

import ca.taterland.tatercertified.overseer.mixin.v1_18.vanilla.accessor.StoredUserEntryAccessor;

import com.mojang.authlib.GameProfile;

import net.minecraft.server.players.UserWhiteListEntry;

@SuppressWarnings("unchecked")
public interface StoredUserEntryBridge {
    default GameProfile bridge$getUser(UserWhiteListEntry whiteListEntry) {
        return ((StoredUserEntryAccessor<GameProfile>) whiteListEntry).invoker$getUser();
    }
}
