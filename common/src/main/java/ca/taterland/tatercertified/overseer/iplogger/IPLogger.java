/**
 * Copyright (c) 2024 Dylan Sperrer - dylan@sperrer.ca
 * The project is Licensed under <a href="https://github.com/Tater-Certified/Overseer/blob/dev/LICENSE">MIT</a>
 */
package ca.taterland.tatercertified.overseer.iplogger;

import ca.taterland.tatercertified.overseer.Overseer;
import ca.taterland.tatercertified.overseer.api.events.LogIPEvent;

import com.google.common.collect.Sets;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

public class IPLogger {
    private static final Set<LogIPEvent> pending = Sets.newConcurrentHashSet();
    public static File logFile;

    public static void logIp(LogIPEvent event) {
        pending.add(event);
        if (pending.size() > 100) {
            flush();
        }
    }

    public static void flush() {
        Set<LogIPEvent> events = Sets.newHashSet(pending);
        pending.clear();
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(logFile, true));
            for (LogIPEvent event : events) {
                out.write(
                        event.timestamp()
                                + ","
                                + event.ip()
                                + ","
                                + event.reason().toString()
                                + System.lineSeparator());
            }
            out.close();
        } catch (IOException e) {
            Overseer.logger().error("Failed to write to IP log file", e);
        }
    }
}
