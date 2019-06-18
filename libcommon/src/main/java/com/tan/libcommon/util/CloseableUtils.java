package com.tan.libcommon.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * 操作实现了Closeable接口的资源
 */
public class CloseableUtils {

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
