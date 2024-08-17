/**
 * Copyright (c) 2024 Dylan Sperrer - dylan@sperrer.ca
 * The project is Licensed under <a href="https://github.com/Tater-Certified/Overseer/blob/dev/LICENSE">MIT</a>
 */
package ca.taterland.tatercertified.overseer.mixin.v1_18.forge.ratelimit;

import static ca.taterland.tatercertified.overseer.mixin.v1_18.forge.ratelimit.RateLimitHelper.rejectConnection;

import ca.taterland.tatercertified.overseer.Overseer;
import ca.taterland.tatercertified.overseer.ddos.PlayerListCache;

import dev.neuralnexus.conditionalmixins.annotations.ReqMCVersion;
import dev.neuralnexus.conditionalmixins.annotations.ReqMappings;
import dev.neuralnexus.taterapi.Mappings;
import dev.neuralnexus.taterapi.MinecraftVersion;

import net.minecraft.network.Connection;
import net.minecraft.network.protocol.login.ServerboundHelloPacket;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@ReqMappings(Mappings.SEARGE)
@ReqMCVersion(min = MinecraftVersion.V1_18, max = MinecraftVersion.V1_18_2)
@Mixin(ServerLoginPacketListenerImpl.class)
public abstract class ServerLoginPacketListenerImplMixin {
    @Shadow
    public abstract Connection shadow$getConnection();

    @Inject(method = "handleHello", at = @At("HEAD"), cancellable = true)
    public void onHandleIntention(ServerboundHelloPacket pPacket, CallbackInfo ci) {
        // Return early if in superAttackMode
        if (Overseer.superAttackMode) {
            rejectConnection(shadow$getConnection(), ci);
            return;
        }

        String name = pPacket.getGameProfile().getName();

        // We know these are bad
        if (name.startsWith("FifthColumn")) {
            rejectConnection(shadow$getConnection(), ci);
            return;
        }

        // Check if we know em
        if (PlayerListCache.checkName(name)) return;

        // Rate limit the rest
        if (Overseer.rateLimit > 0) {
            rejectConnection(shadow$getConnection(), ci);
            return;
        }
        Overseer.rateLimit++;
    }
}
