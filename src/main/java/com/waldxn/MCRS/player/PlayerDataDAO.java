package com.waldxn.MCRS.player;

import com.waldxn.MCRS.common.util.LogUtil;
import com.waldxn.MCRS.skill.core.Skill;
import com.waldxn.MCRS.skill.core.SkillFactory;
import com.waldxn.MCRS.skill.core.SkillType;
import com.waldxn.MCRS.skill.manager.DatabaseManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PlayerDataDAO {

    private final DatabaseManager databaseManager;
    private final PlayerManager playerManager;

    public PlayerDataDAO(DatabaseManager databaseManager, PlayerManager playerManager) {
        this.databaseManager = databaseManager;
        this.playerManager = playerManager;
    }

    public void savePlayerSkills(MCRSPlayer player) {
        try (PreparedStatement ps = databaseManager.getConnection().prepareStatement(
                "REPLACE INTO player_skills (uuid, skill, experience) VALUES (?, ?, ?)")) {

            UUID uuid = player.getUUID();
            for (Map.Entry<SkillType, Skill> entry : player.getSkills().entrySet()) {
                ps.setString(1, uuid.toString());
                ps.setString(2, entry.getKey().name());
                ps.setDouble(3, entry.getValue().getExperience());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            LogUtil.severe("Failed to save player: " + player.getName() + ": " + e.getMessage());
        }
    }

    public MCRSPlayer loadPlayerFromSQL(UUID uuid) {
        MCRSPlayer player = new MCRSPlayer(uuid);
        HashMap<SkillType, Skill> skills = new HashMap<>();

        try (PreparedStatement ps = databaseManager.getConnection().prepareStatement(
                "SELECT skill, experience FROM player_skills WHERE uuid = ?")) {

            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                try {
                    SkillType type = SkillType.valueOf(rs.getString("skill"));
                    double xp = rs.getDouble("experience");
                    Skill skill = SkillFactory.createSkill(type);
                    skill.setInitialExperience(xp);
                    skills.put(type, skill);
                } catch (Exception e) {
                    LogUtil.warning("Invalid skill data for UUID " + uuid + ": " + e.getMessage());
                }
            }
        } catch (SQLException e) {
            LogUtil.severe("Failed to load player: " + player.getName());
            e.printStackTrace();
        }

        // Fill in any missing skills
        for (SkillType type : SkillType.values()) {
            if (type == SkillType.HITPOINTS) {
                Skill hitpoints = SkillFactory.createSkill(type);
                hitpoints.setInitialExperience(1154);
                skills.putIfAbsent(type, hitpoints);
                continue;
            }
            skills.putIfAbsent(type, SkillFactory.createSkill(type));
        }

        player.setSkills(skills);
        return player;
    }

    public List<Map.Entry<MCRSPlayer, Double>> getLeaderboard(SkillType skillType) {
        Map<UUID, Double> xpMap = new HashMap<>();

        try (PreparedStatement ps = databaseManager.getConnection().prepareStatement(
                "SELECT uuid, experience FROM player_skills WHERE skill = ?")) {

            ps.setString(1, skillType.name());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                UUID uuid = UUID.fromString(rs.getString("uuid"));
                double xp = rs.getDouble("experience");
                xpMap.put(uuid, xp);
            }
        } catch (SQLException e) {
            LogUtil.severe("Failed to load leaderboard for skill: " + skillType.name() + ": " + e.getMessage());
            e.printStackTrace();
        }

        // Convert UUID to MCRSPlayer and sort
        return xpMap.entrySet().stream()
                .map(entry -> Map.entry(playerManager.getOrLoad(entry.getKey()), entry.getValue()))
                .filter(entry -> entry.getKey() != null)
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue())) // Descending
                .toList();
    }

    public List<Map.Entry<MCRSPlayer, Double>> getTotalXPLeaderboard() {
        Map<UUID, Double> uuidXpMap = new HashMap<>();

        try (PreparedStatement ps = databaseManager.getConnection().prepareStatement(
                "SELECT uuid, SUM(experience) AS total_xp FROM player_skills GROUP BY uuid")) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                UUID uuid = UUID.fromString(rs.getString("uuid"));
                double totalXp = rs.getDouble("total_xp");
                uuidXpMap.put(uuid, totalXp);
            }
        } catch (SQLException e) {
            LogUtil.severe("Failed to load total XP leaderboard: " + e.getMessage());
            e.printStackTrace();
        }

        return uuidXpMap.entrySet().stream()
                .map(entry -> Map.entry(playerManager.getOrLoad(entry.getKey()), entry.getValue()))
                .filter(entry -> entry.getKey() != null)
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                .toList();
    }
}
