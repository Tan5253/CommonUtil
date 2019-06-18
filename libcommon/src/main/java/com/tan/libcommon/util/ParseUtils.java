package com.tan.libcommon.util;

import android.text.TextUtils;

/**
 * 字符串转换成Double、int、long的工具类
 */
public class ParseUtils {
    public static final double DOUBLE_DEFAULT_VALUE = 0D;
    public static final int INT_DEFAULT_VALUE = 0;
    public static final long LONG_DEFAULT_VALUE = 0L;
    public static final float FLOAT_DEFAULT_VALUE = 0F;

    public static float parseFloat(String text) {
        return parseFloat(text, FLOAT_DEFAULT_VALUE);
    }

    public static float parseFloat(String text, float defValue) {
        float result = defValue;
        if (TextUtils.isEmpty(text)) {
            return result;
        }
        try {
            result = Float.parseFloat(text);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static double parseDouble(String text) {
        return parseDouble(text, DOUBLE_DEFAULT_VALUE);
    }

    public static double parseDouble(String text, double defValue) {
        double result = defValue;
        if (TextUtils.isEmpty(text)) {
            return result;
        }
        try {
            result = Double.parseDouble(text);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static int parseInt(String text) {
        return parseInt(text, INT_DEFAULT_VALUE);
    }

    public static int parseInt(String text, int defValue) {
        int result = defValue;
        if (TextUtils.isEmpty(text)) {
            return result;
        }
        try {
            result = Integer.parseInt(text);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static long parseLong(String text) {
        return parseLong(text, LONG_DEFAULT_VALUE);
    }

    public static long parseLong(String text, long defValue) {
        long result = defValue;
        if (TextUtils.isEmpty(text)) {
            return result;
        }
        try {
            result = Long.parseLong(text);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return result;
    }
}
