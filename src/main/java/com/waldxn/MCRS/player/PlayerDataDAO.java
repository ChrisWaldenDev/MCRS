package com.waldxn.MCRS.player;

import com.waldxn.MCRS.skill.core.Skill;
import com.waldxn.MCRS.skill.core.SkillFactory;
import com.waldxn.MCRS.skill.core.SkillType;
import com.waldxn.MCRS.skill.manager.DatabaseManager;
import org.bukkit.Bukkit;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataDAO {

    public static void savePlayerSkills(MCRSPlayer player) {
        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(
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
            Bukkit.getLogger().severe("Failed to save player: " + player.getName() + ": " + e.getMessage());
        }
    }

    public static MCRSPlayer loadPlayerFromSQL(UUID uuid) {
        MCRSPlayer player = new MCRSPlayer(uuid);
        HashMap<SkillType, Skill> skills = new HashMap<>();

        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(
                "SELECT skill, experience FROM player_skills WHERE uuid = ?")) {

            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                try {
                    SkillType type = SkillType.valueOf(rs.getString("skill"));
                    double xp = rs.getDouble("experience");
                    Skill skill = SkillFactory.createSkill(type);
                    skill.addExperience(xp); // will also recalculate level
                    skills.put(type, skill);
                } catch (Exception e) {
                    Bukkit.getLogger().warning("[MCRS] Invalid skill data for UUID " + uuid + ": " + e.getMessage());
                }
            }
        } catch (SQLException e) {
            Bukkit.getLogger().severe("Failed to load player: " + player.getName());
            e.printStackTrace();
        }

        // Fill in any missing skills
        for (SkillType type : SkillType.values()) {
            skills.putIfAbsent(type, SkillFactory.createSkill(type));
        }

        player.setSkills(skills);
        return player;
    }
}
