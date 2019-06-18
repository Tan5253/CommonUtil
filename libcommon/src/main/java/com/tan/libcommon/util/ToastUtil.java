package com.tan.libcommon.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by Administrator on 2019/6/17.
 */

public class ToastUtil {

    private static Toast toast;

    /**
     * 短时间显示Toast【居下】
     * @param msg 显示的内容-字符串*/
    @SuppressLint("ShowToast")
    public static void shortToast(Context context, String msg) {
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

    /**
     * 短时间显示Toast【居中】
     * @param msg 显示的内容-字符串*/
    @SuppressLint("ShowToast")
    public static void shortToastCenter(Context context, String msg) {
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

    /**
     * 短时间显示Toast【居上】
     * @param msg 显示的内容-字符串*/
    @SuppressLint("ShowToast")
    public static void shortToastTop(Context context, String msg) {
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 0);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }


    /**
     * 长时间显示Toast【居下】
     * @param msg 显示的内容-字符串*/
    @SuppressLint("ShowToast")
    public static void longToast(Context context, String msg) {
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

    /**
     * 长时间显示Toast【居中】
     * @param msg 显示的内容-字符串*/
    @SuppressLint("ShowToast")
    public static void longToastCenter(Context context, String msg) {
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

    /**
     * 长时间显示Toast【居上】
     * @param msg 显示的内容-字符串*/
    @SuppressLint("ShowToast")
    public static void longToastTop(Context context, String msg) {
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 0);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }
}
