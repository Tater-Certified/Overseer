/**
 * Copyright (c) 2024 Dylan Sperrer - dylan@sperrer.ca
 * The project is Licensed under <a href="https://github.com/Tater-Certified/Overseer/blob/dev/LICENSE">MIT</a>
 */
package ca.taterland.tatercertified.overseer.ddos;

import ca.taterland.tatercertified.overseer.api.events.HandleHelloEvent;
import ca.taterland.tatercertified.overseer.api.events.LogIPEvent;
import ca.taterland.tatercertified.overseer.api.events.OverseerEvents;
import ca.taterland.tatercertified.overseer.config.OverseerConfigLoader;

import com.google.common.collect.Sets;

import dev.neuralnexus.taterapi.TaterAPIProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class DDOS {
    private static final DDOS instance = new DDOS();

    public static int rateLimit = 1;
    public static int rate = 1;
    public static boolean logIps = false;

    public static DDOS get() {
        return instance;
    }

    private static final Set<String> usercache = Sets.newConcurrentHashSet();
    private static final List<Supplier<Collection<String>>> nameSources = new ArrayList<>();

    public void addNameSource(Supplier<Collection<String>> source) {
        nameSources.add(source);
    }

    public void refresh() {
        usercache.clear();
        for (Supplier<Collection<String>> source : nameSources) {
            usercache.addAll(source.get());
        }
    }

    public static boolean checkName(String name) {
        return usercache.contains(name);
    }

    public static void handleHello(HandleHelloEvent event) {
        // Check the bad names
        for (String name : OverseerConfigLoader.config().ddos().blacklistedNames()) {
            if (event.name().startsWith(name)) {
                if (logIps) {
                    TaterAPIProvider.scheduler()
                            .runAsync(
                                    () ->
                                            OverseerEvents.LOG_IP.invoke(
                                                    new LogIPEvent(
                                                            event.ip(),
                                                            LogIPEvent.Reason.BLACKLISTED_NAME)));
                }
                event.setCancelled(true);
                return;
            }
        }

        // Check the name cache to see if they should be skipped
        if (checkName(event.name())) return;

        // Rate limit the rest
        if (rate > rateLimit) {
            if (logIps) {
                TaterAPIProvider.scheduler()
                        .runAsync(
                                () ->
                                        OverseerEvents.LOG_IP.invoke(
                                                new LogIPEvent(
                                                        event.ip(),
                                                        LogIPEvent.Reason.RATE_LIMITED)));
            }
            event.setCancelled(true);
            return;
        }
        rate++;
    }
}
