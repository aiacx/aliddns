package cc.lqt.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.regex.Pattern;

@Slf4j
public class IPUtil {

    private static String ApiUrl4 = "http://lqt.cc:8443/api/ip/client";
    private static Pattern IPV4 = Pattern.compile("^((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)$");
    private static Pattern IPV6 = Pattern.compile("^([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$");

    public static void main(String[] args) throws SocketException {
        log.info("ipv4: " + getIpv4());
        log.info("ipv6: " + getIpv6());
    }

    public static void init(String apiUrl4) {
        ApiUrl4 = apiUrl4;
    }

    public static String getIpv4() {
        // 接口返回结果
        String result = "";
        BufferedReader in = null;
        try {
            // 使用HttpURLConnection网络请求第三方接口
            URL url = new URL(ApiUrl4);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            in = new BufferedReader(new InputStreamReader(
                    urlConnection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            log.warn("获取ipv4地址失败，返回值为：" + result);
            return null;
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                log.warn("关闭输入流失败，错误原因：" + e2.getMessage());
            }

        }
        if (!isIpv4(result)) {
            log.warn("获取ipv4地址格式错误，返回值为：" + result);
            return null;
        }
        return result;
    }


    public static String getIpv6() {
        //https://v6r.ipip.net/?format=callback 也可用这个接口返回值
        try {
            InetAddress ads = null;
            Enumeration<?> adds = NetworkInterface.getNetworkInterfaces();
            HashMap<Integer, String> ipv6Map = new HashMap<>();
            int i=0;
            while (adds.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) adds.nextElement();
                Enumeration<?> inetAds = netInterface.getInetAddresses();
                while (inetAds.hasMoreElements()) {
                    ads = (InetAddress) inetAds.nextElement();
                    if (ads instanceof InetAddress && !isReservedAddr(ads)) {//判断是否与InetAddress相同且非保留地址
                        String sb = ads.getHostAddress();//ip
                        if (sb.indexOf("240e") != -1) {
                            ipv6Map.put(i++,sb.split("%")[0]);
                        }
                    }
                }
            }
            if(ipv6Map.size() == 0){
                log.warn("本机无ipv6地址");
                return null;
            }else if(ipv6Map.size()>1){
                //netsh interface ipv6 set privacy state=disable
                log.warn("监测到本机有多个ipv6地址，可能是开启了临时ipv6");
                log.warn("ipv6: "+ ipv6Map);
                log.warn("Windows 用户请输入: netsh interface ipv6 set privacy state=disable 重启后关闭临时ipv6");
                return ipv6Map.get(0);
            }else {
                return ipv6Map.get(0);
            }
        } catch (Exception e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    private static boolean isReservedAddr(InetAddress inetAddr) {
        if (inetAddr.isAnyLocalAddress() || inetAddr.isLinkLocalAddress() || inetAddr.isLoopbackAddress()) {
            return true;
        }
        return false;
    }


    public static boolean isIpv4(String ip4) {
        return IPV4.matcher(ip4).matches();
    }

    public static boolean isIpv6(String ip6) {
        return IPV6.matcher(ip6).matches();
    }
}
