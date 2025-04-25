package com.waldxn.MCRS.util;

import org.bukkit.Bukkit;

public class LogUtil {
    public static void severe(String message) {
        Bukkit.getLogger().severe(message);
    }

    public static void info(String message) {
        Bukkit.getLogger().info(message);
    }

    public static void warning(String message) {
        Bukkit.getLogger().warning(message);
    }
}
