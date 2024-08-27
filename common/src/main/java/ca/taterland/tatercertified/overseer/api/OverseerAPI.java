package ca.taterland.tatercertified.overseer.api;

import ca.taterland.tatercertified.overseer.ddos.PlayerNameCache;

/**
 * API instance for Overseer
 */
public class OverseerAPI {
    private static final OverseerAPI instance = new OverseerAPI();

    private static final PlayerNameCache cache = new PlayerNameCache();
    public static int rateLimit = 1;
    public static int rate = 1;
    public static boolean logIps = false;

    public static OverseerAPI get() {
        return instance;
    }

    public PlayerNameCache cache() {
        return cache;
    }
}
