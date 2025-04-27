package com.waldxn.MCRS;

import com.waldxn.MCRS.command.MCRSCommandManager;
import com.waldxn.MCRS.common.cache.LeaderboardCache;
import com.waldxn.MCRS.common.util.LogUtil;
import com.waldxn.MCRS.listener.*;
import com.waldxn.MCRS.player.MCRSPlayer;
import com.waldxn.MCRS.player.PlayerDataDAO;
import com.waldxn.MCRS.player.PlayerManager;
import com.waldxn.MCRS.skill.manager.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class MCRS extends JavaPlugin {

    PluginManager pm;
    private static ServiceRegistry serviceRegistry;
    private DatabaseManager databaseManager;
    private PlayerDataDAO playerDataDAO;
    private PlayerManager playerManager;
    private LeaderboardCache leaderboardCache;

    @Override
    public void onEnable() {
        // Plugin startup logic
        serviceRegistry = new ServiceRegistry();
        databaseManager = serviceRegistry.getDatabaseManager();
        playerDataDAO = serviceRegistry.getPlayerDataDAO();
        playerManager = serviceRegistry.getPlayerManager();
        leaderboardCache = serviceRegistry.getLeaderboardCache();

        databaseManager.connect();
        pm = getServer().getPluginManager();
        registerEvents();
        MCRSCommandManager.registerCommands(this);

        leaderboardCache.refresh();
        schedulePlayerSync();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        for (MCRSPlayer player : playerManager.getPlayers()) {
            try {
                playerDataDAO.savePlayerSkills(player);
            } catch (Exception e) {
                LogUtil.severe("Failed to save data for " + player.getName() + ": " + e.getMessage());
            }
        }

        if (databaseManager.isConnected()) databaseManager.close();
    }

    private void registerEvents() {
        // Core Listeners
        pm.registerEvents(new PlayerListener(), this);
        pm.registerEvents(new GuiClickListener(), this);
        // Skill Listeners
        pm.registerEvents(new AgilityListener(), this);
        pm.registerEvents(new MeleeListener(), this);
        pm.registerEvents(new RangedListener(), this);
        pm.registerEvents(new ConstructionListener(), this);
        pm.registerEvents(new CookingListener(), this);
        pm.registerEvents(new CraftingListener(), this);
        pm.registerEvents(new FarmingListener(), this);
        pm.registerEvents(new FishingListener(), this);
        pm.registerEvents(new FletchingListener(), this);
    }

    public static MCRS getInstance() {
        return getPlugin(MCRS.class);
    }

    public static ServiceRegistry getServiceRegistry() {
        return serviceRegistry;
    }

    private void schedulePlayerSync() {
        long intervalTicks = 20L * 60 * 5; // 5 minutes in ticks (20 ticks = 1 second)

        if (!databaseManager.isConnected()) return;

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
            int playerCount = 0;

            try {
                for (MCRSPlayer player : playerManager.getPlayers()) {
                    if (player.isDirty()) {
                        playerDataDAO.savePlayerSkills(player);
                        player.clearDirty();
                        playerCount++;
                    }
                }
                if (playerCount > 0) {
                    LogUtil.info("Auto-saved " + playerCount + " player(s) skills.");

                    leaderboardCache.refresh();
                    LogUtil.info("Leaderboard cache refreshed.");
                }
            } catch (Exception e) {
                LogUtil.severe("Auto-save failed: " + e.getMessage());
            }
        }, intervalTicks, intervalTicks);
    }
}
