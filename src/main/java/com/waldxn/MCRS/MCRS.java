package com.waldxn.MCRS;

import com.waldxn.MCRS.cache.LeaderboardCache;
import com.waldxn.MCRS.command.*;
import com.waldxn.MCRS.listener.*;
import com.waldxn.MCRS.player.MCRSPlayer;
import com.waldxn.MCRS.player.PlayerDataDAO;
import com.waldxn.MCRS.player.PlayerManager;
import com.waldxn.MCRS.skill.manager.DatabaseManager;
import com.waldxn.MCRS.util.LogUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class MCRS extends JavaPlugin {

    PluginManager pm;

    @Override
    public void onEnable() {
        // Plugin startup logic
        DatabaseManager.connect();

        pm = getServer().getPluginManager();
        registerEvents();
        registerCommands();

        LeaderboardCache.refresh();
        schedulePlayerSync();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        for (MCRSPlayer player : PlayerManager.getPlayers()) {
            try {
                PlayerDataDAO.savePlayerSkills(player);
            } catch (Exception e) {
                LogUtil.severe("Failed to save data for " + player.getName() + ": " + e.getMessage());
            }
        }

        if (DatabaseManager.isConnected()) DatabaseManager.close();
    }

    private void registerEvents() {
        pm.registerEvents(new PlayerListener(), this);
        pm.registerEvents(new AgilityListener(), this);
        pm.registerEvents(new MeleeListener(), this);
        pm.registerEvents(new RangedListener(), this);
        pm.registerEvents(new ConstructionListener(), this);
        pm.registerEvents(new CookingListener(), this);
        pm.registerEvents(new CraftingListener(), this);
        pm.registerEvents(new GuiClickListener(), this);
        pm.registerEvents(new FarmingListener(), this);
        pm.registerEvents(new FishingListener(), this);
    }

    private void registerCommands() {
        SetStatCommand setStatCommand = new SetStatCommand();
        getCommand("stats").setExecutor(new StatsCommand());
        getCommand("saveall").setExecutor(new SaveAllCommand());
        getCommand("setstat").setExecutor(setStatCommand);
        getCommand("setstat").setTabCompleter(setStatCommand);
        getCommand("wipestats").setExecutor(new WipeStatsCommand());
        getCommand("leaderboard").setExecutor(new LeaderboardCommand());
    }

    public static MCRS getInstance() {
        return getPlugin(MCRS.class);
    }

    private void schedulePlayerSync() {
        long intervalTicks = 20L * 60 * 5; // 5 minutes in ticks (20 ticks = 1 second)

        if (!DatabaseManager.isConnected()) return;

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
            int playerCount = 0;

            try {
                for (MCRSPlayer player : PlayerManager.getPlayers()) {
                    if (player.isDirty()) {
                        PlayerDataDAO.savePlayerSkills(player);
                        player.clearDirty();
                        playerCount++;
                    }
                }
                if (playerCount > 0) {
                    LogUtil.info("Auto-saved " + playerCount + " player(s) skills.");

                    LeaderboardCache.refresh();
                    LogUtil.info("Leaderboard cache refreshed.");
                }
            } catch (Exception e) {
                LogUtil.severe("Auto-save failed: " + e.getMessage());
            }
        },intervalTicks, intervalTicks);
    }
}
