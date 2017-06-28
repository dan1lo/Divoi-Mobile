package com.example.dennis.divoi.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;

/**
 * Created by wolney on 11/04/17.
 */

public class HttpHelper {

    private String urlBase;

    public HttpHelper(String urlBase){
        this.urlBase = urlBase;
    }

    public static boolean hasConnection(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public static boolean isWiFi(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI);
    }

    public static boolean isMobileData(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE);
    }

    public String doGET(String path){
        return doGET(path, null);
    }

    public String doGET(String path, Map<String, String> headers){
        String response = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(getFullURL(path));
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(false);
            if(headers != null && headers.size()>0){
                for(String key : headers.keySet()){
                    connection.addRequestProperty(key, headers.get(key));
                }
            }
            connection.connect();
            if(connection.getResponseCode() >= HttpURLConnection.HTTP_BAD_REQUEST){
                return null;
            }else{
                InputStream in = connection.getInputStream();
                response = IOUtil.toString(in);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            if(connection!=null){
                connection.disconnect();
            }
            return response;
        }
    }

    public String doPOST(String path, Map<String, String> headers, String body){
        String response = null;
        HttpURLConnection connection = null;
        OutputStream os = null;
        BufferedWriter bw = null;
        try {
            URL url = new URL(getFullURL(path));
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            if(headers != null && headers.size()>0){
                for(String key : headers.keySet()){
                    connection.addRequestProperty(key, headers.get(key));
                }
            }
            os = connection.getOutputStream();
            bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            bw.write(body);
            bw.flush();
            bw.close();
            os.close();
            connection.connect();
            if(connection.getResponseCode() >= HttpURLConnection.HTTP_BAD_REQUEST){
                return null;
            }else{
                InputStream in = connection.getInputStream();
                response = IOUtil.toString(in);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            if(connection!=null){
                connection.disconnect();
            }
            return response;
        }
    }

    public String getFullURL(String path){
        return urlBase + "/" + path;
    }

}
