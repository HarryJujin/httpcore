package com.zhongan.qa.utils;

import java.util.Random;

public class SimpleRandomUtils {

    public static String getMobileNum() {
        Random random = new Random(System.nanoTime());
        String[] mobileHead = new String[] { "134", "135", "136", "137", "138", "139", "150", "151", "152", "157",
                "158", "159", "187", "188", "147", "130", "131", "132", "155", "156", "185", "186", "145", "133",
                "153", "180", "189" };
        int mobileBody = (int) Math.floor(random.nextDouble() * 89999999 + 10000000);
        int headSign = (int) Math.round(random.nextDouble() * (mobileHead.length - 1));
        return mobileHead[headSign] + String.valueOf(mobileBody);
    }

    public static double getRandomDouble(int digit) {
        Random random = new Random(System.nanoTime());
        double num = random.nextDouble() * Math.pow(10, digit) + Math.pow(10, digit - 1);
        return Math.round(num);
    }
}
