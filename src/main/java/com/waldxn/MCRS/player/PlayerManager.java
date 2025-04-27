package com.waldxn.MCRS.player;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


public class PlayerManager {

    private static final Map<UUID, MCRSPlayer> players = new ConcurrentHashMap<>();
    private PlayerDataDAO  playerDataDAO;

    public MCRSPlayer get(UUID uuid) {
        return players.get(uuid);
    }

    public void setPlayerDataDAO(PlayerDataDAO playerDataDAO) {
        this.playerDataDAO = playerDataDAO;
    }

    public void put(MCRSPlayer player) {
        players.put(player.getUUID(), player);
    }

    public boolean isLoaded(UUID uuid) {
        return players.containsKey(uuid);
    }

    public void remove(UUID uuid) {
        players.remove(uuid);
    }

    // Fallback method
    public MCRSPlayer getOrLoad(UUID uuid) {
        if (isLoaded(uuid)) return get(uuid);
        MCRSPlayer loaded = playerDataDAO.loadPlayerFromSQL(uuid);
        put(loaded);
        return loaded;
    }

    public Collection<MCRSPlayer> getPlayers() {
        return players.values();
    }
}
