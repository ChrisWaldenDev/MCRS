package com.waldxn.MCRS.listener;

import com.waldxn.MCRS.player.PlayerManager;
import com.waldxn.MCRS.skill.core.SkillType;
import com.waldxn.MCRS.skill.manager.SkillManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerHarvestBlockEvent;
import org.bukkit.inventory.ItemStack;

import static com.waldxn.MCRS.util.MaterialGroups.*;

public class FarmingListener implements Listener {

    @EventHandler
    public void onFarm(BlockBreakEvent event) {
        if (!(FARMABLE_BLOCKS.contains(event.getBlock().getType()))) return;

        if (event.getBlock().getType() == Material.MELON_STEM || event.getBlock().getType() == Material.PUMPKIN_STEM) return;

        if (!(event.getBlock().getBlockData() instanceof Ageable crop)) return;

        if (crop.getAge() < crop.getMaximumAge()) return;

        SkillManager.giveExperience(PlayerManager.get(event.getPlayer().getUniqueId()), SkillType.FARMING, 50);
    }

    @EventHandler
    public void onFarmStackable(BlockBreakEvent event) {
        if (!(STACKED_FARMABLE_BLOCKS.contains(event.getBlock().getType()))) return;

        if (!(event.getBlock().getBlockData() instanceof Ageable)) return;

        int amountHarvested = countGrowthStack(event.getBlock());

        SkillManager.giveExperience(PlayerManager.get(event.getPlayer().getUniqueId()), SkillType.FARMING, 50 * amountHarvested);
    }

    @EventHandler
    public void onHarvest(PlayerHarvestBlockEvent event) {
        if (!(HARVESTABLE_BLOCKS.contains(event.getHarvestedBlock().getType()))) return;

        event.getPlayer().sendMessage("You harvested a block: " + event.getHarvestedBlock().getType());

        int amountHarvested = 0;
        for (ItemStack item : event.getItemsHarvested()) {
            amountHarvested = item.getAmount();
        }

        // TODO: Customize amount of exp for all events
        SkillManager.giveExperience(PlayerManager.get(event.getPlayer().getUniqueId()), SkillType.FARMING, 50 * amountHarvested);
    }

    private int countGrowthStack(Block crop) {
        if (!STACKED_FARMABLE_BLOCKS.contains(crop.getType())) return 0;

        //Returns 0 if the block below is not a farmable block
        if (!STACKED_FARMABLE_BLOCKS.contains(crop.getRelative(0, -1, 0).getType())) return 0;

        int count = 1;
        Block above = crop.getRelative(0,1,0);

        while (STACKED_FARMABLE_BLOCKS.contains(above.getType())) {
            count++;
            above = above.getRelative(0,1,0);
        }

        return count;
    }
}
