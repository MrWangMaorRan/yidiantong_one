package com.yidiantong.util;




import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

    /**
     * 文件工具类
     *
     * @author mazhanzhu 2018年8月19日
     */
    public class FileUtils {
        /**
         * 分隔符.
         */
        public final static String FILE_EXTENSION_SEPARATOR = ".";

        /**
         * 判断手机是否安装某个应用
         *
         * @param context
         * @param appPackageName 应用包名
         * @return true：安装，false：未安装
         */
        public static boolean isApplicationAvilible(Context context, String appPackageName) {
            try {
                // 获取packagemanager
                PackageManager packageManager = context.getPackageManager();
                // 获取所有已安装程序的包信息
                List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
                if (pinfo != null) {
                    for (int i = 0; i < pinfo.size(); i++) {
                        String pn = pinfo.get(i).packageName;
                        if (appPackageName.equals(pn)) {
                            return true;
                        }
                    }
                }
                return false;
            } catch (Exception e) {
                //出现异常，直接打开应用，避免停留在这个页面不动
                return true;
            }
        }

        /**
         * 格式转换文件大小
         *
         * @param context
         * @param fileSize
         * @return
         */
        @SuppressLint("NewApi")
        public static String formatFileSize(Context context, long fileSize) {
            return Formatter.formatFileSize(context, fileSize);
        }

        /**
         * 判断该文件是否存在
         *
         * @return
         */
        public static boolean isExist(String path) {
            try {
                File f = new File(path);
                if (f.exists()) {
                    return true;
                }
            } catch (Exception e) {
                return false;
            }
            return false;

        }

        /**
         * 保存异常信息到文件
         *
         * @param data
         * @param file_name
         * @param isAppend  是否是追加， true为追加。  false为覆盖
         */
        public static void saveFile(String data, String file_name, boolean isAppend) {
            //内置sd卡路径
            File sdPath = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
            if (!sdPath.exists()) {
                sdPath.mkdirs();
            }
            Log.e("fd", "saveFile: " + sdPath.toString());
            File file = new File(sdPath, file_name);
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file, isAppend);
                fos.write(data.getBytes("UTF-8"));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public static void saveFile(String data, String file_name) {
            saveFile(data, file_name, false);
        }

        public static void saveLog(String data, String file_name) {
            String time = new SimpleDateFormat("yy-MM-dd HH:mm:ss").format(new Date());
            saveFile("\n" + time + "\n" + data, file_name, true);
        }

        /**
         * 根据路径删除文件
         */
        public static boolean deleteFile(File file) {
            if (file == null) {
                throw new NullPointerException("file is null");
            }
            if (!file.exists()) {
                return true;
            }
            if (file.isFile()) {
                return file.delete();
            }
            if (!file.isDirectory()) {
                return false;
            }

            File[] files = file.listFiles();
            if (files == null) {
                return true;
            }
            for (File f : files) {
                if (f.isFile()) {
                    f.delete();
                } else if (f.isDirectory()) {
                    deleteFile(f.getAbsolutePath());
                }
            }
            return file.delete();
        }

        /**
         * 判断SD卡是否可用
         *
         * @return SD卡可用返回true
         */
        public static boolean hasSdcard() {
            String status = Environment.getExternalStorageState();
            return Environment.MEDIA_MOUNTED.equals(status);
        }

        /**
         * 删除指定文件或指定目录内的所有文件
         *
         * @param path 文件或目录的绝对路径
         * @return 路径为空或空白字符串，返回true；文件不存在，返回true；文件删除返回true；
         * 文件删除异常返回false
         */
        public static boolean deleteFile(String path) {
            if (TextUtils.isEmpty(path)) {
                return true;
            }
            return deleteFile(new File(path));
        }

        /**
         * 删除指定目录中特定的文件
         *
         * @param dir
         * @param filter
         */
        public static void delete(String dir, FilenameFilter filter) {
            if (TextUtils.isEmpty(dir)) {
                return;
            }
            File file = new File(dir);
            if (!file.exists()) {
                return;
            }
            if (file.isFile()) {
                file.delete();
            }
            if (!file.isDirectory()) {
                return;
            }

            File[] lists = null;
            if (filter != null) {
                lists = file.listFiles(filter);
            } else {
                lists = file.listFiles();
            }
            if (lists == null) {
                return;
            }
            for (File f : lists) {
                if (f.isFile()) {
                    f.delete();
                }
            }
        }

        /**
         * 获得不带扩展名的文件名称
         *
         * @param filePath 文件路径
         * @return
         */
        public static String getFileNameWithoutExtension(String filePath) {
            if (TextUtils.isEmpty(filePath)) {
                return filePath;
            }
            int extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
            int filePosi = filePath.lastIndexOf(File.separator);
            if (filePosi == -1) {
                return (extenPosi == -1 ? filePath : filePath.substring(0,
                        extenPosi));
            }
            if (extenPosi == -1) {
                return filePath.substring(filePosi + 1);
            }
            return (filePosi < extenPosi ? filePath.substring(filePosi + 1,
                    extenPosi) : filePath.substring(filePosi + 1));
        }

    }
