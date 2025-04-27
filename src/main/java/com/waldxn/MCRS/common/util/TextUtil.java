package com.waldxn.MCRS.common.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class TextUtil {

    public static Component xpGainBossBar(double amount, Component title) {
        return Component.text()
                .append(Component.text("+" + (int) amount + " XP", NamedTextColor.GREEN))
                .append(Component.text(" → ", NamedTextColor.WHITE))
                .append(title)
                .build();
    }

    /*
     * Translates legacy '&' color codes into an Adventure Component.
     */
    public static Component translate(String message) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(message);
    }

    /*
     * Serializes a Component back into a legacy-colored String with '§' codes.
     * Useful for Bukkit APIs that still expect Strings (like inventory titles).
     */
    public static String serialize(Component component) {
        return LegacyComponentSerializer.legacySection().serialize(component);
    }
}
