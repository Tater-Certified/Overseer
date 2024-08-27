/**
 * Copyright (c) 2024 Dylan Sperrer - dylan@sperrer.ca
 * The project is Licensed under <a href="https://github.com/Tater-Certified/Overseer/blob/dev/LICENSE">MIT</a>
 */
package ca.taterland.tatercertified.overseer.api.events;

import dev.neuralnexus.taterapi.event.api.EventManager;

public interface OverseerEvents {
    EventManager<HandleHelloEvent> HANDLE_HELLO = new EventManager<>(HandleHelloEvent.class);
    EventManager<LogIPEvent> LOG_IP = new EventManager<>(LogIPEvent.class);
}
