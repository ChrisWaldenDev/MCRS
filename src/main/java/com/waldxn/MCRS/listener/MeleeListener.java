package com.waldxn.MCRS.listener;

import com.waldxn.MCRS.player.PlayerManager;
import com.waldxn.MCRS.skill.core.SkillType;
import com.waldxn.MCRS.skill.manager.SkillManager;
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

public class MeleeListener implements Listener {

    private final Map<UUID, Long> lastXP = new HashMap<>();
    private final SkillManager skillManager;
    private final PlayerManager playerManager;

    public MeleeListener(SkillManager skillManager, PlayerManager playerManager) {
        this.skillManager = skillManager;
        this.playerManager = playerManager;
    }

    @EventHandler
    public void onMelee(EntityDamageByEntityEvent event) {

        //TODO: Include some passive mobs
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Monster) {
            UUID uuid = event.getDamager().getUniqueId();
            long now = System.currentTimeMillis();
            long last = lastXP.getOrDefault(uuid, 0L);

            if (now - last < 500) return;
            if (event.getDamage() <= 0) return;
            if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK &&
                    event.getCause() != EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) return;

            lastXP.put(uuid, now);

            var player = playerManager.get(uuid);
            if (player != null) {
                skillManager.giveExperience(player, SkillType.MELEE, event.getFinalDamage() * 4); // XP awarded every 0.5 seconds, in the amount of final damage done after all damage reductions are applied * 4
                skillManager.giveExperience(player, SkillType.HITPOINTS, event.getFinalDamage() * 1.33); // XP awarded every 0.5 seconds, in the amount of final damage done after all damage reductions are applied * 1.33
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        lastXP.remove(event.getPlayer().getUniqueId());
    }
}
