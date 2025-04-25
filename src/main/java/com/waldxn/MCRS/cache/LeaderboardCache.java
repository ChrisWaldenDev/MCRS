package com.waldxn.MCRS.cache;

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

    public static void refresh() {
        for (SkillType skillType : SkillType.values()) {
            skillLeaderboards.put(skillType, PlayerDataDAO.getLeaderboard(skillType));
        }
        totalXPLeaderboard = PlayerDataDAO.getTotalXPLeaderboard();
    }

    public static List<Map.Entry<MCRSPlayer, Double>> getLeaderboard(SkillType skillType) {
        return skillLeaderboards.getOrDefault(skillType, List.of());
    }

    public static List<Map.Entry<MCRSPlayer, Double>> getTotalXPLeaderboard() {
        return totalXPLeaderboard;
    }
}
