/**
 * Copyright (c) 2024 Dylan Sperrer - dylan@sperrer.ca
 * The project is Licensed under <a href="https://github.com/Tater-Certified/Overseer/blob/dev/LICENSE">MIT</a>
 */
package ca.taterland.tatercertified.overseer.ddos;

import ca.taterland.tatercertified.overseer.Overseer;

import java.net.SocketAddress;

public class ConnectionHandler {
    public static void handleHello(String name, SocketAddress ip, Runnable rejectCallback) {
        // We know these are bad
        if (name.startsWith("FifthColumn")) {
            rejectCallback.run();
            return;
        }

        // Check if we know em
        if (PlayerListCache.checkName(name)) return;

        // Rate limit the rest
        if (Overseer.rateLimit > 0) {
            rejectCallback.run();
            return;
        }
        Overseer.rateLimit++;
    }
}
