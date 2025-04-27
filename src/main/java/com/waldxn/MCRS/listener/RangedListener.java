package com.waldxn.MCRS.listener;

import com.waldxn.MCRS.player.PlayerManager;
import com.waldxn.MCRS.skill.core.SkillType;
import com.waldxn.MCRS.skill.manager.SkillManager;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RangedListener implements Listener {

    private final Map<UUID, Long> lastXP = new HashMap<>();
    private final SkillManager skillManager;
    private final PlayerManager playerManager;

    public RangedListener(SkillManager skillManager, PlayerManager playerManager) {
        this.skillManager = skillManager;
        this.playerManager = playerManager;
    }

    @EventHandler
    public void onRangedAttack(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Arrow arrow)) return;
        if (!(event.getEntity() instanceof Monster)) return;
        if (event.getCause() != EntityDamageEvent.DamageCause.PROJECTILE) return;
        if (!(arrow.getShooter() instanceof Player)) return;

        UUID uuid = ((Player) arrow.getShooter()).getUniqueId();
        long now = System.currentTimeMillis();
        long last = lastXP.getOrDefault(uuid, 0L);

        if (now - last < 500) return;
        if (event.getCause() != EntityDamageEvent.DamageCause.PROJECTILE) return;
        if (event.getDamage() <= 0) return;

        lastXP.put(uuid, now);

        var player = playerManager.get(uuid);
        if (player != null) {
            event.getEntity().sendMessage("2");

            skillManager.giveExperience(player, SkillType.RANGED, event.getFinalDamage() * 4); // XP awarded every 0.5 seconds, in the amount of final damage done after all damage reductions are applied * 4
            skillManager.giveExperience(player, SkillType.HITPOINTS, event.getFinalDamage() * 1.33); // XP awarded every 0.5 seconds, in the amount of final damage done after all damage reductions are applied * 1.33

        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        lastXP.remove(event.getPlayer().getUniqueId());
    }
}
