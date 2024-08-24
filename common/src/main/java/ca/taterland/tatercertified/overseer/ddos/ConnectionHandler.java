/**
 * Copyright (c) 2024 Dylan Sperrer - dylan@sperrer.ca
 * The project is Licensed under <a href="https://github.com/Tater-Certified/Overseer/blob/dev/LICENSE">MIT</a>
 */
package ca.taterland.tatercertified.overseer.ddos;

import ca.taterland.tatercertified.overseer.Overseer;
import ca.taterland.tatercertified.overseer.api.events.HandleHelloEvent;
import ca.taterland.tatercertified.overseer.config.OverseerConfigLoader;

public class ConnectionHandler {
    public static void handleHello(HandleHelloEvent event) {
        // Check the bad names
        for (String badName : OverseerConfigLoader.config().ddos().badNames()) {
            if (event.name().startsWith(badName)) {
                event.setCancelled(true);
                return;
            }
        }

        // Check the name cache to see if they should be skipped
        if (PlayerNameCache.checkName(event.name())) return;

        // Rate limit the rest
        if (Overseer.rate > Overseer.rateLimit) {
            event.setCancelled(true);
            return;
        }
        Overseer.rate++;
    }
}
