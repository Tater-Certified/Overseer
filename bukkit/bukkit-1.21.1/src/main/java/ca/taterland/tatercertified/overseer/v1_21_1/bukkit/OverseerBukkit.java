/**
 * Copyright (c) 2024 Dylan Sperrer - dylan@sperrer.ca
 * The project is Licensed under <a href="https://github.com/Tater-Certified/Overseer/blob/dev/LICENSE">MIT</a>
 */
package ca.taterland.tatercertified.overseer.v1_21_1.bukkit;

import ca.taterland.tatercertified.overseer.api.events.HandleHelloEvent;
import ca.taterland.tatercertified.overseer.api.events.OverseerEvents;
import ca.taterland.tatercertified.overseer.config.OverseerConfigLoader;
import ca.taterland.tatercertified.overseer.config.sections.DDOSConfig;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

import dev.neuralnexus.taterloader.Loader;

import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class OverseerBukkit {
    public OverseerBukkit() {
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        protocolManager.addPacketListener(new HandshakePacketListener());
    }

    static class HandshakePacketListener extends PacketAdapter {
        public HandshakePacketListener() {
            super(
                    (Plugin) Loader.instance().plugin(),
                    ListenerPriority.HIGHEST,
                    PacketType.Login.Client.START);
        }

        private static final String overseer$reason = "Failed to verify username!";

        @SuppressWarnings("deprecation")
        private void overseer$rejectConnection(PacketEvent event) {
            event.getPlayer().kickPlayer(overseer$reason);
            event.setCancelled(true);
        }

        private final List<String> overseer$trashTalk = new ArrayList<>();

        private int overseer$index = 0;

        @SuppressWarnings("deprecation")
        private void overseer$trashTalkReject(PacketEvent event) {
            this.overseer$index++;
            if (this.overseer$index > this.overseer$trashTalk.size()) {
                this.overseer$index = 0;
            }
            event.getPlayer().kickPlayer(this.overseer$trashTalk.get(this.overseer$index));
            event.setCancelled(true);
        }

        @Override
        public void onPacketReceiving(PacketEvent event) {
            DDOSConfig ddos = OverseerConfigLoader.config().ddos();
            if (this.overseer$trashTalk.isEmpty() && ddos.trashTalkEnabled()) {
                this.overseer$trashTalk.addAll(ddos.trashTalk());
            }
            HandleHelloEvent helloEvent;
            if (ddos.trashTalkEnabled()) {
                helloEvent =
                        new HandleHelloEvent(
                                event.getPacket().getStrings().read(0),
                                event.getPlayer().getAddress(),
                                () -> this.overseer$trashTalkReject(event));
            } else {
                helloEvent =
                        new HandleHelloEvent(
                                event.getPacket().getStrings().read(0),
                                event.getPlayer().getAddress(),
                                () -> this.overseer$rejectConnection(event));
            }
            OverseerEvents.HANDLE_HELLO.invoke(helloEvent);
        }
    }
}
