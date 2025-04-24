package com.waldxn.MCRS.util;

import org.bukkit.Material;

import java.util.EnumSet;
import java.util.Set;

public class MaterialGroups {

    public static final Set<Material> FARMABLE_BLOCKS = EnumSet.of(
            Material.WHEAT,
            Material.CARROTS,
            Material.POTATOES,
            Material.BEETROOTS,
            Material.NETHER_WART,
            Material.MELON_STEM,
            Material.PUMPKIN_STEM,
            Material.MELON,
            Material.PUMPKIN
    );

    public static final Set<Material> HARVESTABLE_BLOCKS = EnumSet.of (
            Material.SWEET_BERRY_BUSH
    );

    public static final Set<Material> STACKED_FARMABLE_BLOCKS = EnumSet.of(
            Material.CACTUS,
            Material.SUGAR_CANE
    );

    public static final Set<Material> COOKABLE_FOOD = EnumSet.of(
            Material.POTATO,
            Material.KELP,
            Material.CHICKEN,
            Material.COD,
            Material.MUTTON,
            Material.PORKCHOP,
            Material.RABBIT,
            Material.SALMON,
            Material.BEEF
    );

    public static final Set<Material> CRAFTABLE_FOOD = EnumSet.of(
            Material.RABBIT_STEW,
            Material.BREAD,
            Material.MUSHROOM_STEW,
            Material.COOKIE,
            Material.PUMPKIN_PIE,
            Material.BEETROOT_SOUP,
            Material.SUSPICIOUS_STEW,
            Material.HONEY_BOTTLE,
            Material.CAKE,
            Material.GOLDEN_CARROT,
            Material.GOLDEN_APPLE
    );
}
