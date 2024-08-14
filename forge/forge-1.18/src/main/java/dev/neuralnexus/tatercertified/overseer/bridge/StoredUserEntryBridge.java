package dev.neuralnexus.tatercertified.overseer.bridge;

import com.mojang.authlib.GameProfile;
import dev.neuralnexus.tatercertified.overseer.mixin.accessor.StoredUserEntryAccessor;
import net.minecraft.server.players.UserWhiteListEntry;

public interface StoredUserEntryBridge {
    default GameProfile bridge$getUser(UserWhiteListEntry whiteListEntry) {
        return ((StoredUserEntryAccessor<GameProfile>) whiteListEntry).invoker$getUser();
    }
}
