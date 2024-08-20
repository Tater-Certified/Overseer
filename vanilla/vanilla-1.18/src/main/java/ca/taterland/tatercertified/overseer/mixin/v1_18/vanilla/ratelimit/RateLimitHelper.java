/**
 * Copyright (c) 2024 Dylan Sperrer - dylan@sperrer.ca
 * The project is Licensed under <a href="https://github.com/Tater-Certified/Overseer/blob/dev/LICENSE">MIT</a>
 */
package ca.taterland.tatercertified.overseer.mixin.v1_18.vanilla.ratelimit;

import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.login.ClientboundLoginDisconnectPacket;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class RateLimitHelper {
    public static final Component reason =
            new TranslatableComponent("multiplayer.disconnect.unverified_username");

    public static final ClientboundLoginDisconnectPacket disconnectPacket =
            new ClientboundLoginDisconnectPacket(reason);

    public static void rejectConnection(Connection conn, CallbackInfo ci) {
        conn.send(disconnectPacket);
        conn.disconnect(reason);
        ci.cancel();
    }
}
