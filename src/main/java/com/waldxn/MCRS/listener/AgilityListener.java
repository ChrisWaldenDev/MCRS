package com.waldxn.MCRS.listener;

import com.waldxn.MCRS.player.PlayerManager;
import com.waldxn.MCRS.skill.core.SkillType;
import com.waldxn.MCRS.skill.manager.SkillManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AgilityListener implements Listener {

    private final Map<UUID, Long> lastXP = new HashMap<>();
    private final SkillManager skillManager;
    private final PlayerManager playerManager;

    public AgilityListener(SkillManager skillManager, PlayerManager playerManager) {
        this.skillManager = skillManager;
        this.playerManager = playerManager;
    }

    //Awards Player XP if they are Sprinting, Jumping, or Climbing
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        long now = System.currentTimeMillis();
        long last = lastXP.getOrDefault(uuid, 0L);

        if (now - last < 500) return;

        // Determines if the user is sprinting, jumping, or climbing in order to award xp
        boolean sprinting = event.getPlayer().isSprinting();
        boolean jumping = event.getFrom().getY() < event.getTo().getY();
        boolean climbing = event.getPlayer().isClimbing();

        if (!sprinting && !jumping && !climbing) return;

        // Adds the player to a map to prevent spamming xp gains
        lastXP.put(uuid, now);

        var player = playerManager.get(uuid);
        if (player != null) {
            skillManager.giveExperience(player, SkillType.AGILITY, 10.0); // 10 XP awarded every 0.5 seconds TODO: Possibly configurable?
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        lastXP.remove(event.getPlayer().getUniqueId());
    }
}
