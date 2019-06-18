package com.tan.libcommon.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

/**
 * 文件工具类
 */
public class FileUtils {

    public static void deleteFile(File file) {
        if (file == null || !file.exists()) {
            LogUtil.e("需要删除的文件不存在...");
            return;
        }
        if (file.isFile()) { // 判断是否是文件
            file.delete();
        } else if (file.isDirectory()) { // 否则如果它是一个目录
            File files[] = file.listFiles(); // 获取目录下所有的文件 files[];
            for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
            }
        }
    }

    public static void deleteFile(String fileName) {
        deleteFile(new File(fileName));
    }

    public static String readFile(String fileName) {
        File file = new File(fileName);
        StringBuilder sb = null;
        BufferedReader br = null;
        try {
            if (!file.exists() || file.isDirectory())
                throw new FileNotFoundException();

            br = new BufferedReader(new FileReader(file));
            sb = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line + " ");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb != null ? sb.toString() : "";
    }

    public static void writeFile(String fileName, String content) {
        File file = new File(fileName);
        if (file.isDirectory()) {
            throw new RuntimeException("目录文件夹不能写");
        }
        BufferedWriter bw = null;
        try {
            if (!file.exists())
                file.createNewFile();
            bw = new BufferedWriter(new FileWriter(file));
            bw.write(content);
            bw.newLine();
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void writeFile(String fileName, InputStream inputStream) {
        File file = new File(fileName);
        if (file.isDirectory()) {
            throw new RuntimeException("目录文件夹不能写");
        }

        FileOutputStream fos = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            // 打开一个已存在文件的输出流
            fos = new FileOutputStream(file);
            // 将输入流写入文件输出流中
            int bytesRead;
            byte[] buffer = new byte[2048];
            while ((bytesRead = inputStream.read(buffer, 0, 2048)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            CloseableUtils.close(inputStream);
            CloseableUtils.close(fos);
        }
    }

}
