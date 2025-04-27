package com.waldxn.MCRS.listener;

import com.waldxn.MCRS.MCRS;
import com.waldxn.MCRS.player.MCRSPlayer;
import com.waldxn.MCRS.player.PlayerManager;
import com.waldxn.MCRS.skill.core.SkillType;
import com.waldxn.MCRS.skill.manager.SkillManager;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;


public class FishingListener implements Listener {

    private final SkillManager skillManager = MCRS.getServiceRegistry().getSkillManager();
    private final PlayerManager playerManager = MCRS.getServiceRegistry().getPlayerManager();

    @EventHandler
    public void onFish(PlayerFishEvent event) {
        if (event.getState() != PlayerFishEvent.State.CAUGHT_FISH) return;

        MCRSPlayer player = playerManager.get(event.getPlayer().getUniqueId());

        Entity caught = event.getCaught();
        if (!(caught instanceof Item)) return;

        if (!isFish(((Item) caught).getItemStack().getType())) return;

        skillManager.giveExperience(player, SkillType.FISHING, 50);
    }

    private boolean isFish(Material type) {
        return switch (type) {
            case COD, SALMON, TROPICAL_FISH, PUFFERFISH -> true;
            default -> false;
        };
    }
}
