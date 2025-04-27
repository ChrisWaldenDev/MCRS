package com.waldxn.MCRS.util;

import com.waldxn.MCRS.MCRS;

public class LogUtil {
    public static void severe(String message) {
        MCRS.getInstance().getLogger().severe(message);
    }

    public static void info(String message) {
        MCRS.getInstance().getLogger().info(message);
    }

    public static void warning(String message) {
        MCRS.getInstance().getLogger().warning(message);
    }
}
