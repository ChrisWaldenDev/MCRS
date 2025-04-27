package com.waldxn.MCRS.common.cache;

import com.waldxn.MCRS.player.MCRSPlayer;
import com.waldxn.MCRS.player.PlayerDataDAO;
import com.waldxn.MCRS.skill.core.SkillType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeaderboardCache {

    private static final Map<SkillType, List<Map.Entry<MCRSPlayer, Double>>> skillLeaderboards = new HashMap<>();
    private static List<Map.Entry<MCRSPlayer, Double>> totalXPLeaderboard = new ArrayList<>();

    private final PlayerDataDAO playerDataDAO;

    public LeaderboardCache(PlayerDataDAO playerDataDAO) {
        this.playerDataDAO = playerDataDAO;
    }

    public void refresh() {
        for (SkillType skillType : SkillType.values()) {
            skillLeaderboards.put(skillType, playerDataDAO.getLeaderboard(skillType));
        }
        totalXPLeaderboard = playerDataDAO.getTotalXPLeaderboard();
    }

    public List<Map.Entry<MCRSPlayer, Double>> getLeaderboard(SkillType skillType) {
        return skillLeaderboards.getOrDefault(skillType, List.of());
    }

    public List<Map.Entry<MCRSPlayer, Double>> getTotalXPLeaderboard() {
        return totalXPLeaderboard;
    }
}
