package com.waldxn.MCRS.listener;

import com.waldxn.MCRS.MCRS;
import com.waldxn.MCRS.player.MCRSPlayer;
import com.waldxn.MCRS.player.PlayerDataDAO;
import com.waldxn.MCRS.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;


public class PlayerListener implements Listener {

    private final PlayerDataDAO playerDataDAO;
    private final PlayerManager playerManager;

    public PlayerListener(PlayerDataDAO playerDataDAO, PlayerManager playerManager) {
        this.playerDataDAO = playerDataDAO;
        this.playerManager = playerManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();

        if (playerManager.isLoaded(uuid)) return; // Player is already loaded

        Bukkit.getScheduler().runTaskAsynchronously(MCRS.getInstance(), () -> {
            MCRSPlayer loaded = playerDataDAO.loadPlayerFromSQL(uuid);
            Bukkit.getScheduler().runTask(MCRS.getInstance(), () -> playerManager.put(loaded));
        });
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        MCRSPlayer player = playerManager.get(uuid);
        if (player != null && player.isDirty()) {
            Bukkit.getScheduler().runTaskAsynchronously(MCRS.getInstance(), () -> playerDataDAO.savePlayerSkills(player));
        }
        playerManager.remove(uuid);
    }
}