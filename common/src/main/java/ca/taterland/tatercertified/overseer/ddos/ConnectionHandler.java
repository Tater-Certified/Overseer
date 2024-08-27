/**
 * Copyright (c) 2024 Dylan Sperrer - dylan@sperrer.ca
 * The project is Licensed under <a href="https://github.com/Tater-Certified/Overseer/blob/dev/LICENSE">MIT</a>
 */
package ca.taterland.tatercertified.overseer.ddos;

import ca.taterland.tatercertified.overseer.Overseer;
import ca.taterland.tatercertified.overseer.api.OverseerAPI;
import ca.taterland.tatercertified.overseer.api.events.HandleHelloEvent;
import ca.taterland.tatercertified.overseer.api.events.LogIPEvent;
import ca.taterland.tatercertified.overseer.api.events.OverseerEvents;
import ca.taterland.tatercertified.overseer.config.OverseerConfigLoader;

import dev.neuralnexus.taterapi.TaterAPIProvider;

public class ConnectionHandler {
    public static void handleHello(HandleHelloEvent event) {
        // Check the bad names
        for (String name : OverseerConfigLoader.config().ddos().blacklistedNames()) {
            if (event.name().startsWith(name)) {
                if (OverseerAPI.logIps) {
                    TaterAPIProvider.scheduler()
                            .runAsync(
                                    () ->
                                            OverseerEvents.LOG_IP.invoke(
                                                    new LogIPEvent(
                                                            event.ip(), LogIPEvent.BLACKLISTED_NAME)));
                }
                event.setCancelled(true);
                return;
            }
        }

        // Check the name cache to see if they should be skipped
        if (PlayerNameCache.checkName(event.name())) return;

        // Rate limit the rest
        if (OverseerAPI.rate > OverseerAPI.rateLimit) {
            if (OverseerAPI.logIps) {
                TaterAPIProvider.scheduler()
                        .runAsync(
                                () ->
                                        OverseerEvents.LOG_IP.invoke(
                                                new LogIPEvent(event.ip(), LogIPEvent.RATE_LIMITED)));
            }
            event.setCancelled(true);
            return;
        }
        OverseerAPI.rate++;
    }
}
