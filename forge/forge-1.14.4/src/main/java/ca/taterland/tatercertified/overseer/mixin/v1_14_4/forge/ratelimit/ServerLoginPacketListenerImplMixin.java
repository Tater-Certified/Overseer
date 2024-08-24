/**
 * Copyright (c) 2024 Dylan Sperrer - dylan@sperrer.ca
 * The project is Licensed under <a href="https://github.com/Tater-Certified/Overseer/blob/dev/LICENSE">MIT</a>
 */
package ca.taterland.tatercertified.overseer.mixin.v1_14_4.forge.ratelimit;

import ca.taterland.tatercertified.overseer.api.events.HandleHelloEvent;
import ca.taterland.tatercertified.overseer.api.events.OverseerEvents;

import dev.neuralnexus.conditionalmixins.annotations.ReqMCVersion;
import dev.neuralnexus.conditionalmixins.annotations.ReqMappings;
import dev.neuralnexus.taterapi.Mappings;
import dev.neuralnexus.taterapi.MinecraftVersion;

import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.login.ClientboundLoginDisconnectPacket;
import net.minecraft.network.protocol.login.ServerboundHelloPacket;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@ReqMappings(Mappings.SEARGE)
@ReqMCVersion(min = MinecraftVersion.V1_14, max = MinecraftVersion.V1_16_5)
@Mixin(ServerLoginPacketListenerImpl.class)
public abstract class ServerLoginPacketListenerImplMixin {
    @Shadow
    public abstract Connection shadow$getConnection();

    @Unique private final Component overseer$reason =
            new TranslatableComponent("multiplayer.disconnect.unverified_username");

    @Unique private final ClientboundLoginDisconnectPacket overseer$disconnectPacket =
            new ClientboundLoginDisconnectPacket(overseer$reason);

    @Unique private void overseer$rejectConnection(Connection conn, CallbackInfo ci) {
        conn.send(overseer$disconnectPacket);
        conn.disconnect(overseer$reason);
        ci.cancel();
    }

    @Inject(method = "handleHello", at = @At("HEAD"), cancellable = true)
    public void onHandleIntention(ServerboundHelloPacket packet, CallbackInfo ci) {
        OverseerEvents.HANDLE_HELLO.invoke(
                new HandleHelloEvent(
                        packet.getGameProfile().getName(),
                        this.shadow$getConnection().getRemoteAddress(),
                        () -> this.overseer$rejectConnection(this.shadow$getConnection(), ci)));
    }
}
