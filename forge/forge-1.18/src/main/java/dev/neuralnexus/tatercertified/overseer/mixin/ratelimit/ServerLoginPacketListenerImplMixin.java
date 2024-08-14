package dev.neuralnexus.tatercertified.overseer.mixin.ratelimit;

import dev.neuralnexus.tatercertified.overseer.OverseerForge;
import dev.neuralnexus.tatercertified.overseer.PlayerListCache;
import dev.neuralnexus.tatercertified.overseer.bridge.GameProfileCacheBridge;

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

@Mixin(ServerLoginPacketListenerImpl.class)
public abstract class ServerLoginPacketListenerImplMixin implements GameProfileCacheBridge {
    @Shadow public abstract Connection shadow$getConnection();

    @Unique private static final Component ddosfix$reason = new TranslatableComponent("multiplayer.disconnect.unverified_username");

    @Unique private static final ClientboundLoginDisconnectPacket ddosfix$disconnectPacket = new ClientboundLoginDisconnectPacket(ddosfix$reason);

    @Unique private void ddosfix$rejectConnection(CallbackInfo ci) {
        this.shadow$getConnection().send(ddosfix$disconnectPacket);
        this.shadow$getConnection().disconnect(ddosfix$reason);
        ci.cancel();
    }

    @Inject(method = "handleHello", at = @At("HEAD"), cancellable = true)
    public void onHandleIntention(ServerboundHelloPacket pPacket, CallbackInfo ci) {
        // Return early if in ohSheit mode
        if (OverseerForge.ohSheit) return;

        String name = pPacket.getGameProfile().getName();

        // We know these are bad
        if (name.startsWith("FifthColumn")) {
            this.ddosfix$rejectConnection(ci);
            return;
        }

        // Check if we know em
        if (PlayerListCache.checkName(name)) return;

        // Rate limit the rest
        if (OverseerForge.rateLimit > 0) {
            this.ddosfix$rejectConnection(ci);
            return;
        }
        OverseerForge.rateLimit++;
    }
}
