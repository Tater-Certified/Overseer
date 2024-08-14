package dev.neuralnexus.tatercertified.overseer.mixin.ratelimit;

import dev.neuralnexus.tatercertified.overseer.OverseerForge;
import net.minecraft.network.Connection;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.handshake.ClientIntentionPacket;
import net.minecraft.network.protocol.login.ClientboundLoginDisconnectPacket;
import net.minecraft.server.network.ServerHandshakePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerHandshakePacketListenerImpl.class)
public abstract class ServerHandshakePacketListenerImplMixin {
    @Shadow public abstract Connection shadow$getConnection();

    @Unique
    private static final Component ddosfix$reason = new TranslatableComponent("multiplayer.disconnect.unverified_username");

    @Unique
    private static final ClientboundLoginDisconnectPacket ddosfix$disconnectPacket = new ClientboundLoginDisconnectPacket(ddosfix$reason);

    @Inject(method = "handleIntention", at = @At("HEAD"), cancellable = true)
    public void onHandleIntention(ClientIntentionPacket pPacket, CallbackInfo ci) {
        if (!OverseerForge.ohSheit) return;
        if (pPacket.getIntention() != ConnectionProtocol.LOGIN) return;
        if (OverseerForge.rateLimit > 0) {
            this.shadow$getConnection().send(ddosfix$disconnectPacket);
            this.shadow$getConnection().disconnect(ddosfix$reason);
            ci.cancel();
            return;
        }
        OverseerForge.rateLimit++;
    }
}
