package com.waldxn.MCRS.listener;

import com.waldxn.MCRS.MCRS;
import com.waldxn.MCRS.player.PlayerManager;
import com.waldxn.MCRS.skill.core.SkillType;
import com.waldxn.MCRS.skill.manager.SkillManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BrewingStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class HerbloreListener implements Listener {

    private final HashMap<Location, Player> brewing = new HashMap<>();
    private final HashMap<Location, Long> brewingTime = new HashMap<>();
    private final SkillManager skillManager;
    private final PlayerManager playerManager;

    public HerbloreListener(SkillManager skillManager, PlayerManager playerManager) {
        this.skillManager = skillManager;
        this.playerManager = playerManager;
        cleanReservedBrewingStands();
    }

    @EventHandler
    public void onBrew(BrewEvent event) {
        Location location = event.getBlock().getLocation();
        if (!brewing.containsKey(location)) return;
        Player player = brewing.get(location);
        skillManager.giveExperience(playerManager.get(player.getUniqueId()), SkillType.HERBLORE, 50 * (event.getContents().getSize() - 2));
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;

        // Checks if the player is using a campfire
        if (!(event.getClickedBlock().getState() instanceof BrewingStand brewingStand)) return;

        // Checks if the player is using right click
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Player player = event.getPlayer();
        Location location = brewingStand.getLocation();

        // Checks if another player is already using the brewing stand
        if (brewing.containsKey(location)) {
            Player brewingPlayer = brewing.get(location);
            if (!brewingPlayer.getName().equalsIgnoreCase(player.getName())) {
                // Player tried to interact with a brewing stand currently reserved for another player
                event.setCancelled(true);
                player.sendMessage(Component.text(brewingPlayer.getName(), NamedTextColor.GOLD)
                        .append(Component.text(" is already brewing there!", NamedTextColor.DARK_RED)));
            } else {
                // Refresh reserve timer
                brewingTime.put(location, System.currentTimeMillis());
            }
        } else {
            // Reserve the brewing stand for the player
            player.sendMessage(Component.text("This brewing stand is now reserved for you temporarily", NamedTextColor.GRAY));
            brewing.put(location, player);
            brewingTime.put(location, System.currentTimeMillis());
        }
    }

    @EventHandler
    public void onBrewingStandBreak(BlockBreakEvent event) {
        if (!(event.getBlock().getState() instanceof BrewingStand brewingStand)) return;

        Location location = brewingStand.getLocation();

        if (!brewing.containsKey(location)) return;

        if (!brewingTime.containsKey(location)) return;

        brewingTime.remove(location);
        brewing.remove(location);
    }

    @EventHandler
    public void onBrewingStandClose(InventoryCloseEvent event) {
        if (event.getInventory().getHolder() instanceof BrewingStand brewingStand) {

            Location location = brewingStand.getLocation();

            if (!brewing.containsKey(location)) return;

            boolean isEmpty = true;
            for (ItemStack i : brewingStand.getInventory().getStorageContents()) {
                if (i != null && i.getType() != Material.BLAZE_POWDER) {
                    isEmpty = false;
                    break;
                }
            }

            if (isEmpty) {
                brewing.remove(location);
                brewingTime.remove(location);
            }
        }
    }

    private void cleanReservedBrewingStands() {
        Bukkit.getScheduler().runTaskTimer(MCRS.getInstance(), () -> {
            long currentTime = System.currentTimeMillis();
            for (Map.Entry<Location, Player> entry : brewing.entrySet()) {
                Location location = entry.getKey();

                if (location.getBlock().getState() instanceof BrewingStand) {
                    long time = brewingTime.get(location);

                    if (currentTime - time > 120000) { // 2 Minutes (120 seconds)
                        Player brewingPlayer = brewing.get(location);
                        brewingPlayer.sendMessage(Component.text("A brewing stand is no longer reserved for you!", NamedTextColor.GRAY));
                        brewing.remove(location);
                        brewingTime.remove(location);
                    }

                }
            }
        }, 2400, 2400); // 2 minutes in ticks
    }
}
