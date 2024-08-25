/**
 * Copyright (c) 2024 Dylan Sperrer - dylan@sperrer.ca
 * The project is Licensed under <a href="https://github.com/Tater-Certified/Overseer/blob/dev/LICENSE">MIT</a>
 */
package ca.taterland.tatercertified.overseer.mixin.v1_19.vanilla.ratelimit;

import ca.taterland.tatercertified.overseer.api.events.HandleHelloEvent;
import ca.taterland.tatercertified.overseer.api.events.OverseerEvents;
import ca.taterland.tatercertified.overseer.config.OverseerConfigLoader;
import ca.taterland.tatercertified.overseer.config.sections.DDOSConfig;

import dev.neuralnexus.conditionalmixins.annotations.ReqMCVersion;
import dev.neuralnexus.conditionalmixins.annotations.ReqMappings;
import dev.neuralnexus.taterapi.Mappings;
import dev.neuralnexus.taterapi.MinecraftVersion;

import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.login.ClientboundLoginDisconnectPacket;
import net.minecraft.network.protocol.login.ServerboundHelloPacket;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@ReqMappings(Mappings.MOJMAP)
@ReqMCVersion(min = MinecraftVersion.V1_19, max = MinecraftVersion.V1_19_3)
@Mixin(ServerLoginPacketListenerImpl.class)
public abstract class ServerLoginPacketListenerImplMixin {
    @Shadow
    public abstract Connection shadow$getConnection();

    @Unique private final Component overseer$reason =
            Component.translatable("multiplayer.disconnect.unverified_username");

    @Unique private final ClientboundLoginDisconnectPacket overseer$disconnectPacket =
            new ClientboundLoginDisconnectPacket(overseer$reason);

    @Unique private void overseer$rejectConnection(Connection conn, CallbackInfo ci) {
        conn.send(overseer$disconnectPacket);
        conn.disconnect(overseer$reason);
        ci.cancel();
    }

    @Unique private final List<Component> overseer$trashTalk = new ArrayList<>();

    @Unique private final List<ClientboundLoginDisconnectPacket> overseer$trashTalkPackets =
            new ArrayList<>();

    @Unique private int overseer$index = 0;

    @Unique private void overseer$trashTalkReject(Connection conn, CallbackInfo ci) {
        this.overseer$index++;
        if (this.overseer$index > this.overseer$trashTalk.size()) {
            this.overseer$index = 0;
        }
        conn.send(this.overseer$trashTalkPackets.get(this.overseer$index));
        conn.disconnect(this.overseer$trashTalk.get(this.overseer$index));
        ci.cancel();
    }

    @Inject(method = "handleHello", at = @At("HEAD"), cancellable = true)
    public void onHandleHello(ServerboundHelloPacket packet, CallbackInfo ci) {
        DDOSConfig ddos = OverseerConfigLoader.config().ddos();
        if (this.overseer$trashTalk.isEmpty()) {
            for (String trash : ddos.trashTalk()) {
                Component component = Component.literal(trash);
                this.overseer$trashTalk.add(component);
                this.overseer$trashTalkPackets.add(new ClientboundLoginDisconnectPacket(component));
            }
        }
        HandleHelloEvent event;
        if (ddos.trashTalkEnabled()) {
            event =
                    new HandleHelloEvent(
                            packet.name(),
                            this.shadow$getConnection().getRemoteAddress(),
                            () -> this.overseer$trashTalkReject(this.shadow$getConnection(), ci));
        } else {
            event =
                    new HandleHelloEvent(
                            packet.name(),
                            this.shadow$getConnection().getRemoteAddress(),
                            () -> this.overseer$rejectConnection(this.shadow$getConnection(), ci));
        }
        OverseerEvents.HANDLE_HELLO.invoke(event);
    }
}
