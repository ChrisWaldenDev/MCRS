package com.waldxn.MCRS.common.util;

import org.bukkit.Material;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.*;

public class CraftingUtil {

    // Function to handle shift-click crafting
    public static int getCraftedAmount(CraftItemEvent event) {
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
