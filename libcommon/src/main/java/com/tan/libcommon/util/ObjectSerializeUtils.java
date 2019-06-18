package com.tan.libcommon.util;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 对象序列化工具类
 */
public class ObjectSerializeUtils {

    /**
     * 删除序列化的对象，即删除相应文件
     *
     * @param fileName
     */
    public static void clearObject(Context context, String fileName) {
        Context applicationContext = context.getApplicationContext();
        FileUtils.deleteFile(applicationContext.getFilesDir() + "/" + fileName);
    }

    /**
     * 序列化对象
     *
     * @param context
     * @param fileName 存储的文件名，从Constants类中获取
     * @param obj
     */
    public static void saveObject(Context context, String fileName, Object obj) {
        Context applicationContext = context.getApplicationContext();
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream(applicationContext.getFilesDir() + "/" + fileName));
            oos.writeObject(obj);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 读取对象
     *
     * @param context
     * @param fileName 存储的文件名
     * @return
     */
    public static Object getObject(Context context, String fileName) {
        Context applicationContext = context.getApplicationContext();
        Object result = null;
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(applicationContext.getFilesDir() + "/" + fileName));
            result = ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return result;
    }
}
