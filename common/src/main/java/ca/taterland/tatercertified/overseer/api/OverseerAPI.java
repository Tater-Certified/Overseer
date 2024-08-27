/**
 * Copyright (c) 2024 Dylan Sperrer - dylan@sperrer.ca
 * The project is Licensed under <a href="https://github.com/Tater-Certified/Overseer/blob/dev/LICENSE">MIT</a>
 */
package ca.taterland.tatercertified.overseer.api;

import ca.taterland.tatercertified.overseer.ddos.DDOS;

import org.jetbrains.annotations.ApiStatus;

/** API instance for Overseer */
public class OverseerAPI {
    private static OverseerAPI instance;
    private final DDOS ddos;

    public OverseerAPI() {
        ddos = new DDOS();
    }

    public static OverseerAPI get() {
        return instance;
    }

    @ApiStatus.Internal
    public static void register(OverseerAPI api) {
        if (instance == null) {
            throw new IllegalStateException(
                    "The Overseer API has not been registered yet, you're most likely trying to access it too early in loading.");
        }
        instance = api;
    }

    @ApiStatus.Internal
    public static void unregister() {
        instance = null;
    }

    public DDOS ddos() {
        return this.ddos;
    }
}
