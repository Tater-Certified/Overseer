/**
 * Copyright (c) 2024 Dylan Sperrer - dylan@sperrer.ca
 * The project is Licensed under <a href="https://github.com/Tater-Certified/Overseer/blob/dev/LICENSE">MIT</a>
 */
package ca.taterland.tatercertified.overseer.ddos;

import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class PlayerNameCache {
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
}
