package com.waldxn.MCRS.listener;

import com.waldxn.MCRS.MCRS;
import com.waldxn.MCRS.player.PlayerManager;
import com.waldxn.MCRS.skill.core.SkillType;
import com.waldxn.MCRS.skill.manager.SkillManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.waldxn.MCRS.common.util.MaterialGroups.FARMABLE_BLOCKS;
import static com.waldxn.MCRS.common.util.MaterialGroups.STACKED_FARMABLE_BLOCKS;

public class ConstructionListener implements Listener {

    private final Map<UUID, Long> lastXP = new HashMap<>();
    private final SkillManager skillManager = MCRS.getServiceRegistry().getSkillManager();
    private final PlayerManager playerManager = MCRS.getServiceRegistry().getPlayerManager();

    @EventHandler
    public void onPlayerBuild(BlockPlaceEvent event) {
        // Doesn't give XP for placing farmable blocks
        if (FARMABLE_BLOCKS.contains(event.getBlock().getType()) || STACKED_FARMABLE_BLOCKS.contains(event.getBlock().getType()))
            return;

        UUID uuid = event.getPlayer().getUniqueId();
        long now = System.currentTimeMillis();
        long last = lastXP.getOrDefault(uuid, 0L);

        if (now - last < 250) return;

        // Adds the player to a map to prevent spamming xp gains
        lastXP.put(uuid, now);

        var player = playerManager.get(uuid);
        if (player != null) {
            skillManager.giveExperience(player, SkillType.CONSTRUCTION, 50); // XP awarded every 0.25 seconds
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        lastXP.remove(event.getPlayer().getUniqueId());
    }
}
