package com.future.link.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2018/8/10.
 */
public class HttpsGetUtil {

    public static String doHttpsGetJson(String Url)
    {
        String message = "";
        try
        {
            System.out.println("doHttpsGetJson");//TODO:dd
            URL urlGet = new URL(Url);
            HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
            http.setRequestMethod("GET");      //必须是get方式请求    24
            http.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            http.setDoOutput(true);
            http.setDoInput(true);
            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");//连接超时30秒28
            System.setProperty("sun.net.client.defaultReadTimeout", "30000"); //读取超时30秒29 30
            http.connect();
            InputStream is =http.getInputStream();
            int size =is.available();
            byte[] jsonBytes =new byte[size];
            is.read(jsonBytes);
            message=new String(jsonBytes,"UTF-8");
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return message;
    }
    
    private final static int CONNECT_TIMEOUT = 5000; // in milliseconds
	private final static String DEFAULT_ENCODING = "UTF-8";
 
	public static String postData(String urlStr, String data) {
		return postData(urlStr, data, null);
	}
 
	public static String postData(String urlStr, String data, String contentType) {
		BufferedReader reader = null;
		try {
			URL url = new URL(urlStr);
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			conn.setConnectTimeout(CONNECT_TIMEOUT);
			conn.setReadTimeout(CONNECT_TIMEOUT);
			if (contentType != null)
				conn.setRequestProperty("content-type", contentType);
			OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream(), DEFAULT_ENCODING);
			if (data == null)
				data = "";
			writer.write(data);
			writer.flush();
			writer.close();
 
			reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), DEFAULT_ENCODING));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
				sb.append("\r\n");
			}
			return sb.toString();
		} catch (IOException e) {
		} finally {
			try {
				if (reader != null)
					reader.close();
			} catch (IOException e) {
			}
		}
		return null;
	}
	/**
	 * 获取真实ip地址 通过阿帕奇代理的也能获取到真实ip
	 * @param request
	 * @return
	 */
	public static String getRealIp(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
}
