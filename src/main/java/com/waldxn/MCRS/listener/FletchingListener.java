package com.waldxn.MCRS.listener;

import com.waldxn.MCRS.MCRS;
import com.waldxn.MCRS.common.util.CraftingUtil;
import com.waldxn.MCRS.player.PlayerManager;
import com.waldxn.MCRS.skill.core.SkillType;
import com.waldxn.MCRS.skill.manager.SkillManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;

import static com.waldxn.MCRS.common.util.MaterialGroups.FLETCHING;

public class FletchingListener implements Listener {

    private final SkillManager skillManager = MCRS.getServiceRegistry().getSkillManager();
    private final PlayerManager playerManager = MCRS.getServiceRegistry().getPlayerManager();

    @EventHandler
    public void onFletch(CraftItemEvent event) {
        if (!FLETCHING.contains(event.getRecipe().getResult().getType())) return;

        if (!(event.getWhoClicked() instanceof Player player)) return;

        int amountCrafted = 1;

        // Handles shift-click crafting
        if (event.isShiftClick()) {
            amountCrafted = CraftingUtil.getCraftedAmount(event);
        } else if (event.getCurrentItem() != null) {
            amountCrafted = event.getCurrentItem().getAmount();
        }

        // TODO: Customize the amount of XP given based on the item crafted (config)
        skillManager.giveExperience(playerManager.get(player.getUniqueId()), SkillType.FLETCHING, 25 * amountCrafted);
    }
}
