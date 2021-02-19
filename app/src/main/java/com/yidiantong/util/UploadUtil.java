package com.yidiantong.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UploadUtil {
    public static final String ENCORDING = "utf-8";
    /**
     * 文件上传
     * @param filepath 文件完整路径，包括文件名
     * @param serverUrl 上传的服务器地址
     * @return
     */
    public static boolean upload(String filepath,String serverUrl) {
        //起始标记分隔线
        String boundary = "---------------------------7db1c523809b2";//+java.util.UUID.randomUUID().toString();//
        //将完整路径封装成文件
        File file = new File(filepath);
        //从完整路径中获取文件名称
        String name = filepath.substring(filepath.lastIndexOf("/")+1);
        String fileName = null;
        try {
            fileName = new String(name.getBytes(), "ISO-8859-1");
            //拼接url
            URL url = new URL(serverUrl + "?filename=" + fileName + "&filetype=IMAGE");
            // 用来拼接请求
            StringBuilder sb = new StringBuilder();
        /*// username字段
        sb.append("--" + boundary + "\r\n");
        sb.append("Content-Disposition: form-data; name=\"username\"" + "\r\n");
        sb.append("\r\n");
        sb.append(username + "\r\n");

        // password字段
        sb.append("--" + boundary + "\r\n");
        sb.append("Content-Disposition: form-data; name=\"password\"" + "\r\n");
        sb.append("\r\n");
        sb.append(password + "\r\n");*/

            // 文件部分
            sb.append("--" + boundary + "\r\n");
            sb.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + filepath + "\"" + "\r\n");
            //此Content-Type代表二进制流，不知道下载文件类型
            sb.append("Content-Type: application/octet-stream" + "\r\n");
            sb.append("\r\n");

            // 将开头和结尾部分转为字节数组，因为设置Content-Type时长度是字节长度
            byte[] before = sb.toString().getBytes(ENCORDING);
            byte[] after = ("\r\n--" + boundary + "--\r\n").getBytes(ENCORDING);

            // 打开连接, 设置请求头
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(10000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            conn.setRequestProperty("Content-Length", before.length + file.length() + after.length + "");

            conn.setDoOutput(true);//允许输出流
            conn.setDoInput(true);//允许输出流

            // 获取输入输出流
            OutputStream out = conn.getOutputStream();
            FileInputStream fis = new FileInputStream(file);
            // 将开头部分写出
            out.write(before);

            // 写出文件数据
            byte[] buf = new byte[1024 * 5];
            int len;
            while ((len = fis.read(buf)) != -1)
                out.write(buf, 0, len);

            // 将结尾部分写出
            out.write(after);
            //获取服务器过来的输入流
            InputStream in = conn.getInputStream();
            InputStreamReader isReader = new InputStreamReader(in);
            BufferedReader bufReader = new BufferedReader(isReader);
            String line = null;
            String data = "getResult=";
            while ((line = bufReader.readLine()) != null)
                data += line;
            Log.e("fromServer", "result=" + data);
            boolean sucess = conn.getResponseCode() == 200;
            in.close();
            fis.close();
            out.close();
            conn.disconnect();
            return sucess;
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return false;
    }
}
