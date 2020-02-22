package com.hlh.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 网络相关
 */

public class NetUtil {

    private static Logger logger = LogManager.getLogger(NetUtil.class);

    /**
     * 获取请求头中的IP地址
     *
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = null;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if (ipAddress.equals("127.0.0.1")) {
                    // 根据网卡取本机配置的IP
                    InetAddress inet = null;
                    try {
                        inet = InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                        logger.error("UnknownHostException", e);
                    }
                    ipAddress = inet.getHostAddress();
                }
            }

            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
                // = 15
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            logger.error("Exception", e);
            ipAddress = "";
        }

        return ipAddress;
    }

    /**
     * 获取本机外网V4 IP地址
     *
     * @return
     */
    public static String getLocalV4IP() {
        String ip = "";
        String chinaz = "https://www.baidu.com";

        StringBuilder inputLine = new StringBuilder();
        String read = "";
        URL url = null;
        HttpURLConnection urlConnection = null;
        BufferedReader in = null;
        try {
            url = new URL(chinaz);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
            while ((read = in.readLine()) != null) {
                inputLine.append(read + "\r\n");
            }
        } catch (MalformedURLException e) {
            logger.error("MalformedURLException", e);
        } catch (IOException e) {
            logger.error("IOException", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error("IOException", e);
                }
            }
        }

        Pattern p = Pattern.compile("\\<dd class\\=\"fz24\">(.*?)\\<\\/dd>");
        Matcher m = p.matcher(inputLine.toString());
        if (m.find()) {
            String ipstr = m.group(1);
            ip = ipstr;
        }

        return ip;
    }

    /**
     * 获取某一个URL对应的服务器IP地址
     *
     * @param strUrl
     * @return
     */
    public static String getWebIp(String strUrl) {
        try {
            URL url = new URL(strUrl);
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    url.openStream()));
            String s = "";
            StringBuffer sb = new StringBuffer("");
            String webContent = "";
            while ((s = br.readLine()) != null) {
                sb.append(s + "rn");
            }
            br.close();

            webContent = sb.toString();
            int start = webContent.indexOf("[") + 1;
            int end = webContent.indexOf("]");
            if (start < 0 || end < 0) {
                return null;
            }
            webContent = webContent.substring(start, end);
            return webContent;
        } catch (Exception e) {
            logger.error("Exception", e);
            return null;
        }
    }

    /**
     * 将ip地址转换为Long型，二进制
     *
     * @param ip
     * @return
     */
    public static Long ipToLong(String ip) {
        if (ip == null) {
            return -1L;
        }

        if (ip.equals("0:0:0:0:0:0:0:1")) {
            return 0L;
        }

        if (ip.length() < 7) {
            return -1L;
        }

        // IPV4
        if (ip.length() < 16) {
            String[] split = ip.split("\\.");
            if (split.length != 4) {
                return -1L;
            }

            return Long.parseLong(split[0]) << 24 | Long.parseLong(split[1]) << 16 | Long.parseLong(split[2]) << 8 | Long.parseLong(split[3]);
        }

        // IPV6
        return 1L;
    }

    public static void main(String[] args) {
        String localV4IP = getWebIp("www.baidu.com");
        System.out.println(localV4IP);
    }
}
