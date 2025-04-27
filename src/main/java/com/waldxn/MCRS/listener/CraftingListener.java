package com.waldxn.MCRS.listener;

import com.waldxn.MCRS.common.util.CraftingUtil;
import com.waldxn.MCRS.player.PlayerManager;
import com.waldxn.MCRS.skill.core.SkillType;
import com.waldxn.MCRS.skill.manager.SkillManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;

import static com.waldxn.MCRS.common.util.MaterialGroups.CRAFTABLE_FOOD;
import static com.waldxn.MCRS.common.util.MaterialGroups.FLETCHING;

public class CraftingListener implements Listener {

    private final SkillManager skillManager;
    private final PlayerManager playerManager;

    public CraftingListener(SkillManager skillManager, PlayerManager playerManager) {
        this.skillManager = skillManager;
        this.playerManager = playerManager;

    }

    @EventHandler
    public void onPlayerCraft(CraftItemEvent event) {
        // Check if the item is a food item -- Avoids crafting XP where cooking xp is rewarded
        if (CRAFTABLE_FOOD.contains(event.getRecipe().getResult().getType()) ||
                FLETCHING.contains(event.getRecipe().getResult().getType())) return;


        if (!(event.getWhoClicked() instanceof Player player)) return;

        int amountCrafted = 1;

        // Handles shift-click crafting
        if (event.isShiftClick()) {
            amountCrafted = CraftingUtil.getCraftedAmount(event);
        } else if (event.getCurrentItem() != null) {
            amountCrafted = event.getCurrentItem().getAmount();
        }

        // TODO: Customize the amount of XP given based on the item crafted (config)
        skillManager.giveExperience(playerManager.get(player.getUniqueId()), SkillType.CRAFTING, 25 * amountCrafted);
    }
}
