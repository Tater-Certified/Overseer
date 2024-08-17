/**
 * Copyright (c) 2024 Dylan Sperrer - dylan@sperrer.ca
 * The project is Licensed under <a href="https://github.com/Tater-Certified/Overseer/blob/dev/LICENSE">MIT</a>
 */
package ca.taterland.tatercertified.overseer.v1_18.forge;

import static com.mojang.brigadier.arguments.BoolArgumentType.bool;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

import ca.taterland.tatercertified.overseer.Overseer;
import ca.taterland.tatercertified.overseer.v1_18.forge.bridge.GameProfileCacheBridge;
import ca.taterland.tatercertified.overseer.v1_18.forge.bridge.StoredUserEntryBridge;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.players.UserWhiteListEntry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.network.NetworkConstants;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unused")
public class OverseerForge implements GameProfileCacheBridge, StoredUserEntryBridge {
    public OverseerForge() {
        ModLoadingContext.get()
                .registerExtensionPoint(
                        IExtensionPoint.DisplayTest.class,
                        () ->
                                new IExtensionPoint.DisplayTest(
                                        () -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true));

        MinecraftForge.EVENT_BUS.register(this);

        Overseer.cache.addNameSource(
                () -> {
                    MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
                    Set<String> newCache = new HashSet<>();
                    Map<String, ?> cache = this.bridge$getProfilesbyName(server.getProfileCache());
                    newCache.addAll(cache.keySet());
                    for (UserWhiteListEntry user :
                            server.getPlayerList().getWhiteList().getEntries()) {
                        newCache.add(this.bridge$getUser(user).getName());
                    }
                    return newCache;
                });

        Util.backgroundExecutor()
                .execute(
                        () -> {
                            while (true) {
                                if (Overseer.rateLimit > 0) {
                                    Overseer.rateLimit = 0;
                                }
                                try {
                                    Thread.sleep(1000);
                                } catch (Exception ignore) {
                                }
                            }
                        });
    }

    @SubscribeEvent
    public void onRegisterBrigadierCommand(RegisterCommandsEvent event) {
        LiteralArgumentBuilder<CommandSourceStack> argumentBuilder =
                literal("ddos")
                        .requires(source -> source.hasPermission(4))
                        .then(
                                argument("truefalse", bool())
                                        .executes(
                                                context -> {
                                                    Overseer.superAttackMode =
                                                            context.getArgument(
                                                                    "truefalse", Boolean.class);
                                                    context.getSource()
                                                            .sendSuccess(
                                                                    new TextComponent(
                                                                            "§aSet ddos mode to §6"
                                                                                    + Overseer
                                                                                            .superAttackMode),
                                                                    true);
                                                    return Command.SINGLE_SUCCESS;
                                                }));
        event.getDispatcher().register(argumentBuilder);
    }

    @SubscribeEvent
    public void onServerStarted(ServerStartedEvent event) {
        Util.backgroundExecutor()
                .execute(
                        () -> {
                            while (true) {
                                Overseer.cache.refresh();
                                try {
                                    Thread.sleep(30000);
                                } catch (Exception ignore) {
                                }
                            }
                        });
    }
}
