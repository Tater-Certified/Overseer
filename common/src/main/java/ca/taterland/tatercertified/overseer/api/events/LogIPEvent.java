/**
 * Copyright (c) 2024 Dylan Sperrer - dylan@sperrer.ca
 * The project is Licensed under <a href="https://github.com/Tater-Certified/Overseer/blob/dev/LICENSE">MIT</a>
 */
package ca.taterland.tatercertified.overseer.api.events;

import dev.neuralnexus.taterapi.event.Event;

import java.net.SocketAddress;

public class LogIPEvent implements Event {
    public static final String BLACKLISTED_NAME = "Blacklisted username";
    public static final String RATE_LIMITED = "Rate limited";

    private final SocketAddress ip;
    private final String reason;

    public LogIPEvent(SocketAddress ip, String reason) {
        this.ip = ip;
        this.reason = reason;
    }

    public SocketAddress ip() {
        return this.ip;
    }

    public String reason() {
        return this.reason;
    }
}
