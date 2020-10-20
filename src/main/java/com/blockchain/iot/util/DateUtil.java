package com.blockchain.iot.util;

public class DateUtil {

    public static boolean lessThanOneHour(long timestamp) {
        long sixtyMinutes = System.currentTimeMillis() - 2 * 60 * 1000;
        if (timestamp < sixtyMinutes) {
            return true;
        }
        return false;
    }
}
