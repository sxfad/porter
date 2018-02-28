/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月20日 13:50
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.common.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月20日 13:50
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月20日 13:50
 */
public class MachineUtils {
    public static final String HOST_NAME = System.getProperty("user.name");
    public static final long CURRENT_JVM_PID = getPID();
    public static final String IP_ADDRESS = localhost();
    private static final String LOCAL_EXCEPT_IP = "127.0.0.1,192.168.2.1,192.168.122.1";
    public static long getPID() {
        String processName = java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
        if (processName != null && processName.length() > 0) {
            try {
                return Long.parseLong(processName.split("@")[0]);
            }
            catch (Exception e) {
                return 0;
            }
        }
        return 0;
    }
    private static List<String> getLocalInetAddress() {
        List<String> inetAddressList = new ArrayList<String>();
        try {
            Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
            while (enumeration.hasMoreElements()) {
                NetworkInterface networkInterface = enumeration.nextElement();
                Enumeration<InetAddress> addrs = networkInterface.getInetAddresses();
                boolean ifUnUse = networkInterface.isLoopback() || networkInterface.isPointToPoint()
                        || networkInterface.isVirtual() || !networkInterface.isUp();
                while (addrs.hasMoreElements() && !ifUnUse) {
                    inetAddressList.add(addrs.nextElement().getHostAddress());
                }
            }
        }
        catch (SocketException e) {
            throw new RuntimeException("get local inet address fail", e);
        }

        return inetAddressList;
    }

    public static String localhost() {
        List<String>  list = getLocalInetAddress();
        for (String i : list) {
            if (!LOCAL_EXCEPT_IP.contains(i) && i.indexOf(":") < 0) {
                return i;
            }
        }
        return "127.0.0.1";
    }
}
