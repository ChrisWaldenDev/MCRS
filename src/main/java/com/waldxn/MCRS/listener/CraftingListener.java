package com.waldxn.MCRS.listener;

import com.waldxn.MCRS.player.PlayerManager;
import com.waldxn.MCRS.skill.core.SkillType;
import com.waldxn.MCRS.skill.manager.SkillManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.*;

public class CraftingListener implements Listener {

    @EventHandler
    public void onPlayerCraft(CraftItemEvent event) {
        // Check if the item is a food item -- Avoids crafting XP where cooking xp is rewarded
        if (CookingListener.CRAFTABLE_FOOD.contains(event.getRecipe().getResult().getType())) return;

        if (!(event.getWhoClicked() instanceof Player bukkitPlayer)) return;

        int amountCrafted = 1;

        // Handles shift-click crafting
        if (event.isShiftClick()) {
            amountCrafted = getCraftedAmount(event);
        } else if (event.getCurrentItem() != null) {
            amountCrafted = event.getCurrentItem().getAmount();
        }

        // TODO: Customize the amount of XP given based on the item crafted (config)
        SkillManager.giveExperience(PlayerManager.get(bukkitPlayer.getUniqueId()), SkillType.CRAFTING, 25 * amountCrafted);
    }

    // Function to handle shift-click crafting
    private int getCraftedAmount(CraftItemEvent event) {
        CraftingInventory inventory = event.getInventory();
        Recipe recipe = event.getRecipe();

        // Returns 0 if the recipe is not a vanilla recipe
        if (!(recipe instanceof ShapedRecipe) && !(recipe instanceof ShapelessRecipe)) {
            return 0;
        }

        ItemStack[] matrix = inventory.getMatrix();
        int maxCrafts = Integer.MAX_VALUE;

        // Finds the lowest amount of items in an ItemStack in the recipe, to determine how many times the recipe can be crafted
        for (ItemStack item : matrix) {
            if (item != null && item.getType() != Material.AIR) {
                maxCrafts = Math.min(maxCrafts, item.getAmount());
            }
        }
        return maxCrafts * recipe.getResult().getAmount();
    }
}
