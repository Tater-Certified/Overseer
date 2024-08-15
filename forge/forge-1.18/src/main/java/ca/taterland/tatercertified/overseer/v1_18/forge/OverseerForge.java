/**
 * Copyright (c) 2024 Dylan Sperrer - dylan@sperrer.ca
 * The project is Licensed under <a href="https://github.com/Tater-Certified/Overseer/blob/dev/LICENSE">MIT</a>
 */
package ca.taterland.tatercertified.overseer.v1_18.forge;

import static com.mojang.brigadier.arguments.BoolArgumentType.bool;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkConstants;

import java.util.concurrent.ForkJoinPool;

@Mod(OverseerForge.MOD_ID)
public class OverseerForge {
    public static final String MOD_ID = "overseer";
    private static final PlayerListCache cache = new PlayerListCache();
    public static int rateLimit = 0;
    public static boolean ohSheit = false;

    public OverseerForge() {
        ModLoadingContext.get()
                .registerExtensionPoint(
                        IExtensionPoint.DisplayTest.class,
                        () ->
                                new IExtensionPoint.DisplayTest(
                                        () -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true));

        MinecraftForge.EVENT_BUS.register(this);
        ForkJoinPool.commonPool()
                .execute(
                        () -> {
                            while (true) {
                                if (rateLimit > 0) {
                                    rateLimit = 0;
                                }
                                try {
                                    Thread.sleep(1000);
                                } catch (Exception ignore) {
                                }
                            }
                        });
        ForkJoinPool.commonPool()
                .execute(
                        () -> {
                            while (true) {
                                cache.update();
                                try {
                                    Thread.sleep(30000);
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
                                                    ohSheit =
                                                            context.getArgument(
                                                                    "truefalse", Boolean.class);
                                                    context.getSource()
                                                            .sendSuccess(
                                                                    new TextComponent(
                                                                            "§aSet ddos mode to §6"
                                                                                    + ohSheit),
                                                                    true);
                                                    return Command.SINGLE_SUCCESS;
                                                }));
        event.getDispatcher().register(argumentBuilder);
    }
}
