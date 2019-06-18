package com.tan.libcommon.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.Window;

import java.io.File;
import java.util.List;

/**
 * app相关工具类
 * <p>
 * Manifest.permission.WRITE_SETTINGS
 */
public class AppUtils {
    /**
     * 平台类型
     */
    private static final int PLATFORM_TYPE = 0;
    public AppStatus mAppStatus;
    private Context mContext;

    private volatile static AppUtils sInstance = null;

    public static AppUtils getInstance(Context context) {
        if (sInstance == null) {
            synchronized (AppUtils.class) {
                if (sInstance == null) {
                    sInstance = new AppUtils(context);
                }
            }
        }
        return sInstance;
    }

    private AppUtils(Context context) {
        mContext = context.getApplicationContext();
        initAppStatus();
    }

    /**
     * app相关的状态信息
     */
    public static class AppStatus {
        /**
         * 版本号码
         */
        public int versionCode;
        /**
         * 版本名称
         */
        public String versionName;
        /**
         * 渠道号码
         */
        public String downSource;
        /**
         * 包名
         */
        public String packageName;
        /**
         * 平台类型
         */
        public int platformType;
        /**
         * 签名信息
         */
        public String sign;

        @Override
        public String toString() {
            return "AppStatus{" +
                    "versionCode=" + versionCode +
                    ", versionName='" + versionName + '\'' +
                    ", downSource='" + downSource + '\'' +
                    ", packageName='" + packageName + '\'' +
                    ", platformType=" + platformType +
                    ", sign='" + sign + '\'' +
                    '}';
        }
    }

    /**
     * 获取应用相关的状态信息
     */
    private void initAppStatus() {
        mAppStatus = new AppStatus();
        mAppStatus.platformType = PLATFORM_TYPE;
        PackageManager pm = mContext.getPackageManager();
        if (pm != null) {
            try {
                String packageName = mContext.getPackageName();
                PackageInfo pi = pm.getPackageInfo(packageName, 0);
                mAppStatus.packageName = pi.packageName;
                mAppStatus.versionCode = pi.versionCode;
                mAppStatus.versionName = pi.versionName;
            } catch (Exception e) {
                LogUtil.e("获得应用packageName、versionCode、versionName失败 " + e.getMessage());
            }
            try {
                mAppStatus.sign = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_SIGNATURES).signatures[0].toCharsString();
            } catch (Exception e) {
                LogUtil.e("获得应用sign失败 " + e.getMessage());
            }
        }
        try {
            ApplicationInfo appInfo = mContext.getPackageManager().getApplicationInfo(mContext.getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo != null && appInfo.metaData != null) {
                mAppStatus.downSource = appInfo.metaData.getString("UMENG_CHANNEL");
            }
        } catch (Exception e) {
            LogUtil.e("获得应用downSource信息失败 " + e.getMessage());
        }
    }

    /**
     * APP是否正在运行，并且baseActivity为MainActivity
     *
     * @param context
     * @return
     */
    public static boolean isAppRunning(Context context) {
        boolean isRunning = false;
        Context applicationContext = context.getApplicationContext();
        ActivityManager am = (ActivityManager) applicationContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(sInstance.mAppStatus.packageName) &&
                    info.baseActivity.getPackageName().equals(sInstance.mAppStatus.packageName)) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    public static String getBaseActivityName(Context context) {
        Context applicationContext = context.getApplicationContext();
        ActivityManager am = (ActivityManager) applicationContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.baseActivity.getPackageName().equals(sInstance.mAppStatus.packageName)) {
                return info.baseActivity.getClassName();
            }
        }
        return "";
    }

    /**
     * 判断服务是否启动, 注意只要名称相同, 会检测任何服务.
     *
     * @param context
     * @param serviceClass 需要判断的服务类
     * @return
     */
    public static boolean isServiceRunning(Context context, Class<?> serviceClass) {
        Context applicationContext = context.getApplicationContext();
        ActivityManager am = (ActivityManager) applicationContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = am.getRunningServices(Integer.MAX_VALUE);// 参数表示需要获取的正在运行的服务数量，这里我们取最大值
        if (runningServices != null && !runningServices.isEmpty()) {
            for (ActivityManager.RunningServiceInfo r : runningServices) {
                // 添加Uid验证, 防止服务重名, 当前服务无法启动
                if (getUid(context) == r.uid) {
                    if (serviceClass.getName().equals(r.service.getClassName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 获取应用的Uid, 用于验证服务是否启动
     *
     * @param context 上下文
     * @return uid
     */
    private static int getUid(Context context) {
        if (context == null) {
            return -1;
        }

        int pid = android.os.Process.myPid();
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        if (manager != null) {
            List<ActivityManager.RunningAppProcessInfo> infos = manager.getRunningAppProcesses();
            if (infos != null && !infos.isEmpty()) {
                for (ActivityManager.RunningAppProcessInfo processInfo : infos) {
                    if (processInfo.pid == pid) {
                        return processInfo.uid;
                    }
                }
            }
        }
        return -1;
    }

    /**
     * 判断某个activity是否处于前台
     * <uses-permission android:name = "android.permission.GET_TASKS"/>
     *
     * @param context
     * @param cls     需要判断的activity的全类名
     * @return
     */
    @SuppressWarnings("deprecation")
    public static boolean isTopActivity(Context context, String cls) {
        Context applicationContext = context.getApplicationContext();
        ActivityManager am = (ActivityManager) applicationContext.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        if (!applicationContext.getPackageName().equals(cn.getPackageName()) || !cls.equals(cn.getClassName())) {
            return false;
        }
        return true;
    }

    /**
     * 安装文件
     *
     * @param file
     */
    public static void installFile(Context context, File file) {
        Context applicationContext = context.getApplicationContext();
        try {
            Intent installIntent = new Intent();
            Uri uri;
            //判断是否是AndroidN以及更高的版本
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                installIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                uri = FileProvider.getUriForFile(applicationContext, AppUtils.getInstance(applicationContext).mAppStatus.packageName + ".fileprovider", file);
            } else {
                installIntent.setAction(Intent.ACTION_VIEW);
                installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                uri = Uri.fromFile(file);
            }
            installIntent.setDataAndType(uri, "application/vnd.android.package-archive");
            applicationContext.startActivity(installIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 安装文件
     */
    public static void installFile(Context context, Uri uri) {
        Context applicationContext = context.getApplicationContext();
        try {
            Intent installIntent = new Intent();
            //判断是否是AndroidN以及更高的版本
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                installIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                installIntent.setAction(Intent.ACTION_VIEW);
                installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            installIntent.setDataAndType(uri, "application/vnd.android.package-archive");
            applicationContext.startActivity(installIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取通知栏和标题栏的总高度
     *
     * @param activity
     * @return
     */
    public static int getTopBarHeight(Activity activity) {
        return activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
    }

    /**
     * 跳转到应用设置页面
     *
     * @param context
     */
    public static void gotoAppDetailSettingActivity(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        localIntent.setData(Uri.fromParts("package", sInstance.mAppStatus.packageName, null));
        if (context instanceof Activity) {
            ((Activity) context).finish();
        }
        context.startActivity(localIntent);
    }

    /**
     * 获取进程名称
     *
     * @param context 上下文
     * @return 进程名称
     */
    public static String getProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> infos = manager.getRunningAppProcesses();
        if (infos != null) {
            for (ActivityManager.RunningAppProcessInfo processInfo : infos) {
                if (processInfo.pid == pid) {
                    return processInfo.processName;
                }
            }
        }
        return null;
    }

    /**
     * 检测应用是否运行
     *
     * @param packageName 包名
     * @param context     上下文
     * @return 是否存在
     */
    public static boolean isAppAlive(String packageName, Context context) {
        if (context == null || TextUtils.isEmpty(packageName)) {
            return false;
        }

        ActivityManager activityManager = (ActivityManager)
                context.getSystemService(Context.ACTIVITY_SERVICE);

        if (activityManager != null) {
            List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
            if (procInfos != null && !procInfos.isEmpty()) {
                for (int i = 0; i < procInfos.size(); i++) {
                    if (procInfos.get(i).processName.equals(packageName)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * 获取应用图标
     * @param context
     * @param packageName
     * @return
     */
    public static Drawable getAppIcon(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        Drawable appIcon = null;
        try {
            ApplicationInfo applicationInfo = pm.getApplicationInfo(packageName, 0);
            appIcon = applicationInfo.loadIcon(pm);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appIcon;
    }

    /**
     * 获取应用第一次安装日期
     * @param context
     * @param packageName
     * @return
     */
    public static long getAppFirstInstallTime(Context context, String packageName) {
        long lastUpdateTime = 0;
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
            lastUpdateTime = packageInfo.firstInstallTime;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return lastUpdateTime;
    }

    /**
     * 获取应用更新日期
     * @param context
     * @param packageName
     * @return
     */
    public static long getAppLastUpdateTime(Context context, String packageName) {
        long lastUpdateTime = 0;
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
            lastUpdateTime = packageInfo.lastUpdateTime;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return lastUpdateTime;
    }

    /**
     * 获取应用大小
     * @param context
     * @param packageName
     * @return
     */
    public static long getAppSize(Context context, String packageName) {
        long appSize = 0;
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(packageName, 0);
            appSize = new File(applicationInfo.sourceDir).length();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appSize;
    }

    /**
     * 获取应用apk文件
     * @param context
     * @param packageName
     * @return
     */
    public static String getAppApk(Context context, String packageName) {
        String sourceDir = null;
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(packageName, 0);
            sourceDir = applicationInfo.sourceDir;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return sourceDir;
    }

    /**
     * 获取应用的安装市场
     * @param context
     * @param packageName
     * @return
     */
    public static String getAppInstaller(Context context, String packageName) {
        return context.getPackageManager().getInstallerPackageName(packageName);
    }

    /**
     * 获取应用兼容sdk
     * @param context
     * @param packageName
     * @return
     */
    public static int getAppTargetSdkVersion(Context context, String packageName) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            return applicationInfo.targetSdkVersion;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }
    /**
     * 应用是否安装
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isInstalled(Context context, String packageName) {
        boolean installed = false;
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        List<ApplicationInfo> installedApplications = context.getPackageManager().getInstalledApplications(0);
        for (ApplicationInfo in : installedApplications) {
            if (packageName.equals(in.packageName)) {
                installed = true;
                break;
            } else {
                installed = false;
            }
        }
        return installed;
    }

    /**
     * 卸载应用
     * @param context
     * @param packageName
     * @return
     */
    public static boolean uninstallApk(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        Intent i = new Intent(Intent.ACTION_DELETE, Uri.parse("package:" + packageName));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        return true;
    }
}
