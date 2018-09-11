package com.example.qfz.selfservicespace.object;

import android.util.Log;
import com.example.qfz.selfservicespace.config.ServerConfiguration;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Connection {

    public String connectServer(String msg, String operation) {
        User user = null;

        HttpURLConnection connection = null;
        String urlString = "http://" + ServerConfiguration.IP + ":" + ServerConfiguration.PORT + operation;

        try {
            Log.d("Connection", "Connect " + urlString);
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();

            //设置参数
            connection.setDoOutput(true);     //需要输出
            connection.setDoInput(true);      //需要输入
            connection.setUseCaches(false);   //不允许缓存
            connection.setRequestMethod("POST");      //设置POST方式连接

            //设置请求属性
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
            connection.setRequestProperty("Charset", "UTF-8");

            //连接,也可以不用明文connect，使用下面的httpConn.getOutputStream()会自动connect
            connection.connect();

            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            Log.d("Send", msg.toString());
            out.writeBytes(URLEncoder.encode(msg.toString(), "UTF-8"));
            out.flush();
            out.close();

            //获得响应状态
            int resultCode = connection.getResponseCode();
            if (HttpURLConnection.HTTP_OK == resultCode) {
                StringBuffer response = new StringBuffer();
                String readLine = new String();
                BufferedReader responseReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                while ((readLine = responseReader.readLine()) != null) {
                    response.append(readLine).append("\n");
                }
                responseReader.close();
                Log.d("Received", response.toString());
                connection.disconnect();

                return response.toString();
            }
        } catch(Exception e) {
        }
        return "";
    }
}
