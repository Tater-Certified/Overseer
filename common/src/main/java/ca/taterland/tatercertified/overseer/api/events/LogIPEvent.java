/**
 * Copyright (c) 2024 Dylan Sperrer - dylan@sperrer.ca
 * The project is Licensed under <a href="https://github.com/Tater-Certified/Overseer/blob/dev/LICENSE">MIT</a>
 */
package ca.taterland.tatercertified.overseer.api.events;

import dev.neuralnexus.taterapi.event.Event;

import java.net.SocketAddress;

public class LogIPEvent implements Event {
    private final SocketAddress ip;
    private final Reason reason;
    private final long timestamp = System.currentTimeMillis();

    public LogIPEvent(SocketAddress ip, Reason reason) {
        this.ip = ip;
        this.reason = reason;
    }

    public SocketAddress ip() {
        return this.ip;
    }

    public Reason reason() {
        return this.reason;
    }

    public long timestamp() {
        return this.timestamp;
    }

    public enum Reason {
        BLACKLISTED_NAME("Blacklisted username"),
        RATE_LIMITED("Rate limited");

        public final String reason;

        Reason(String reason) {
            this.reason = reason;
        }

        @Override
        public String toString() {
            return this.reason;
        }
    }
}
