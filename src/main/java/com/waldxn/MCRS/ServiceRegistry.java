package com.waldxn.MCRS;

import com.waldxn.MCRS.common.cache.LeaderboardCache;
import com.waldxn.MCRS.common.ui.LeaderboardGui;
import com.waldxn.MCRS.common.ui.SkillsGui;
import com.waldxn.MCRS.player.PlayerDataDAO;
import com.waldxn.MCRS.player.PlayerManager;
import com.waldxn.MCRS.skill.manager.BossBarManager;
import com.waldxn.MCRS.skill.manager.DatabaseManager;
import com.waldxn.MCRS.skill.manager.SkillManager;

public class ServiceRegistry {

    private final SkillManager skillManager;
    private final PlayerManager playerManager;
    private final DatabaseManager databaseManager;
    private final BossBarManager bossBarManager;
    private final PlayerDataDAO playerDataDAO;
    private final LeaderboardCache leaderboardCache;
    private final LeaderboardGui leaderboardGui;
    private final SkillsGui skillsGui;

    public ServiceRegistry() {
        databaseManager = new DatabaseManager();
        bossBarManager = new BossBarManager();
        skillManager = new SkillManager(bossBarManager);
        playerManager = new PlayerManager();
        playerDataDAO = new PlayerDataDAO(databaseManager, playerManager);
        playerManager.setPlayerDataDAO(playerDataDAO);
        leaderboardCache = new LeaderboardCache(playerDataDAO);
        leaderboardGui = new LeaderboardGui(leaderboardCache);
        skillsGui = new SkillsGui();
    }

    public SkillManager getSkillManager() {
        return skillManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    public BossBarManager getBossBarManager() {
        return bossBarManager;
    }

    public PlayerDataDAO getPlayerDataDAO() {
        return playerDataDAO;
    }

    public LeaderboardCache getLeaderboardCache() {
        return leaderboardCache;
    }

    public LeaderboardGui getLeaderboardGui() {
        return leaderboardGui;
    }

    public SkillsGui getSkillsGui() {
        return skillsGui;
    }
}
