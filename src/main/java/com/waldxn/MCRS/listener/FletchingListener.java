package com.waldxn.MCRS.listener;

import com.waldxn.MCRS.player.PlayerManager;
import com.waldxn.MCRS.skill.core.SkillType;
import com.waldxn.MCRS.skill.manager.SkillManager;
import com.waldxn.MCRS.util.CraftingUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;

import static com.waldxn.MCRS.util.MaterialGroups.FLETCHING;

public class FletchingListener implements Listener {

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
        SkillManager.giveExperience(PlayerManager.get(player.getUniqueId()), SkillType.CRAFTING, 25 * amountCrafted);
    }
}
