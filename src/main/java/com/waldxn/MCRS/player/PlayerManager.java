package com.waldxn.MCRS.player;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


public class PlayerManager {

    private static final Map<UUID, MCRSPlayer> players = new ConcurrentHashMap<>();

    public static MCRSPlayer get(UUID uuid) {
        return players.get(uuid);
    }

    public static void put(MCRSPlayer player) {
        players.put(player.getUUID(), player);
    }

    public static boolean isLoaded(UUID uuid) {
        return players.containsKey(uuid);
    }

    public static void remove(UUID uuid) {
        players.remove(uuid);
    }

    // Fallback method
    public static MCRSPlayer getOrLoad(UUID uuid) {
        if (isLoaded(uuid)) return get(uuid);
        MCRSPlayer loaded = PlayerDataDAO.loadPlayerFromSQL(uuid);
        put(loaded);
        return loaded;
    }

    public static Collection<MCRSPlayer> getPlayers() {
        return players.values();
    }
}
