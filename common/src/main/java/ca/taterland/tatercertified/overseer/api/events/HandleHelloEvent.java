/**
 * Copyright (c) 2024 Dylan Sperrer - dylan@sperrer.ca
 * The project is Licensed under <a href="https://github.com/Tater-Certified/Overseer/blob/dev/LICENSE">MIT</a>
 */
package ca.taterland.tatercertified.overseer.api.events;

import dev.neuralnexus.taterapi.event.Cancellable;
import dev.neuralnexus.taterapi.event.Event;

import java.net.SocketAddress;

public class HandleHelloEvent implements Event, Cancellable {
    private final String name;
    private final SocketAddress ip;
    private final Runnable rejectCallback;
    private boolean cancelled;

    public HandleHelloEvent(String name, SocketAddress ip, Runnable rejectCallback) {
        this.name = name;
        this.ip = ip;
        this.rejectCallback = rejectCallback;
    }

    @Override
    public boolean cancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        if (cancelled) {
            this.cancelled = true;
            this.rejectCallback.run();
        }
    }

    public String name() {
        return this.name;
    }

    public SocketAddress ip() {
        return this.ip;
    }
}
