/**
 * Copyright (c) 2024 Dylan Sperrer - dylan@sperrer.ca
 * The project is Licensed under <a href="https://github.com/Tater-Certified/Overseer/blob/dev/LICENSE">MIT</a>
 */
package ca.taterland.tatercertified.overseer.v1_14_4.forge;

import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.network.FMLNetworkConstants;

import org.apache.commons.lang3.tuple.Pair;

@SuppressWarnings("unused")
public class OverseerForge {
    public OverseerForge() {
        ModLoadingContext.get()
                .registerExtensionPoint(
                        ExtensionPoint.DISPLAYTEST,
                        () ->
                                Pair.of(
                                        () -> FMLNetworkConstants.IGNORESERVERONLY,
                                        (incoming, isNetwork) -> true));
    }
}
