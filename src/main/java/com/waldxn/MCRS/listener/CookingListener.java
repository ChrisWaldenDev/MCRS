package com.waldxn.MCRS.listener;

import com.waldxn.MCRS.MCRS;
import com.waldxn.MCRS.common.util.CraftingUtil;
import com.waldxn.MCRS.player.MCRSPlayer;
import com.waldxn.MCRS.player.PlayerManager;
import com.waldxn.MCRS.skill.core.SkillType;
import com.waldxn.MCRS.skill.manager.SkillManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Campfire;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockCookEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

import static com.waldxn.MCRS.common.util.MaterialGroups.COOKABLE_FOOD;
import static com.waldxn.MCRS.common.util.MaterialGroups.CRAFTABLE_FOOD;

public class CookingListener implements Listener {

    private final HashMap<Location, Player> cooking = new HashMap<>();
    private final SkillManager skillManager;
    private final PlayerManager playerManager;

    public CookingListener(SkillManager skillManager, PlayerManager playerManager) {
        this.skillManager = skillManager;
        this.playerManager = playerManager;
    }

    @EventHandler
    public void onBlockCook(BlockCookEvent event) {
        // Checks if the block is a campfire
        if (!(event.getBlock().getState() instanceof Campfire)) return;

        // Checks that the campfire is currently being used by a player
        if (!cooking.containsKey(event.getBlock().getLocation())) return;

        MCRSPlayer player = playerManager.get(cooking.get(event.getBlock().getLocation()).getUniqueId());

        //TODO: Adjust xp based on the item cooked (config)
        skillManager.giveExperience(player, SkillType.COOKING, 50);

        // Scheduler checks if the campfire is empty after a 1 tick delay to ensure BlockState has updated
        Bukkit.getScheduler().runTaskLater(MCRS.getInstance(), () -> {
            Block block = event.getBlock();
            if (!(block.getState() instanceof Campfire fire)) return;

            boolean empty = true;
            for (int i = 0; i <= 3; i++) {
                if (fire.getItem(i) != null) {
                    empty = false;
                }
            }

            if (empty) {
                cooking.remove(event.getBlock().getLocation());
            }
        }, 1L);

    }

    // TODO: Possibly adjust time to cook based on player level
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;

        // Checks if the player is using a campfire
        if (!(event.getClickedBlock().getState() instanceof Campfire campfire)) return;

        // Checks if the player has an item in their hand
        if (event.getItem() == null) return;

        // Checks if the player is using right click
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Location location = event.getClickedBlock().getLocation();
        Player player = event.getPlayer();
        ItemStack itemStack = event.getItem();

        // Checks if the campfire is full
        boolean isFull = true;
        for (int i = 0; i <= 3; i++) {
            if (campfire.getItem(i) == null) {
                isFull = false;
            }
        }

        if (isFull) {
            event.setCancelled(true);
            player.sendMessage(Component.text("The campfire is full!", NamedTextColor.DARK_RED));
            return;
        }

        // Checks if another player is using the campfire
        if (cooking.containsKey(location) && cooking.get(location) != player) {
            event.setCancelled(true);
            player.sendMessage(Component.text(player.getName(), NamedTextColor.GOLD)
                    .append(Component.text(" is already cooking there!", NamedTextColor.DARK_RED)));
            return;
        }

        // Checks if the player is using cookable food
        if (!COOKABLE_FOOD.contains(itemStack.getType())) {
            event.setCancelled(true);
            player.sendMessage(Component.text("You cannot cook that!", NamedTextColor.DARK_RED));
            return;
        }

        // Reserves the campfire for the player in the cooking map
        if (!cooking.containsKey(location)) {
            cooking.put(event.getClickedBlock().getLocation(), event.getPlayer());
        }
    }

    @EventHandler
    public void onFoodCraft(CraftItemEvent event) {
        // Checks if the recipe is for a craftable food
        if (!CRAFTABLE_FOOD.contains(event.getRecipe().getResult().getType())) return;

        if (!(event.getWhoClicked() instanceof Player bukkitPlayer)) return;

        int amountCrafted = 1;

        // Handles shift-click crafting
        if (event.isShiftClick()) {
            amountCrafted = CraftingUtil.getCraftedAmount(event);
        } else if (event.getCurrentItem() != null) {
            amountCrafted = event.getCurrentItem().getAmount();
        }

        // TODO: Customize the amount of XP given based on the item crafted (config)
        skillManager.giveExperience(playerManager.get(bukkitPlayer.getUniqueId()), SkillType.COOKING, 50 * amountCrafted);
    }

    @EventHandler
    public void onCampfireBreak(BlockBreakEvent event) {
        if (!(event.getBlock().getState() instanceof Campfire campfire)) return;

        Location location = campfire.getLocation();

        if (!cooking.containsKey(location)) return;

        cooking.remove(location);
    }
}
