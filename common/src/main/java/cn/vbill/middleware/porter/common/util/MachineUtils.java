/*
 * Copyright ©2018 vbill.cn.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package cn.vbill.middleware.porter.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    /**
     * HOST_NAME
     */
    public static final String HOST_NAME = System.getProperty("user.name");
    /**
     * CURRENT_JVM_PID
     */
    public static final long CURRENT_JVM_PID = getPID();
    /**
     * IP_ADDRESS
     */
    public static final String IP_ADDRESS = localhost();
    /**
     * CPU_NUMBER
     */
    public static final int CPU_NUMBER = Runtime.getRuntime().availableProcessors();
    /**
     * LOCAL_EXCEPT_IP
     */
    private static final String LOCAL_EXCEPT_IP = "127.0.0.1,192.168.2.1,192.168.122.1";

    private static final Logger LOGGER = LoggerFactory.getLogger(MachineUtils.class);

    /**
     * getPID
     * @return
     */
    public static long getPID() {
        String processName = java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
        if (processName != null && processName.length() > 0) {
            try {
                return Long.parseLong(processName.split("@")[0]);
            }
            catch (Exception e) {
                LOGGER.error("%s", e);
                return 0;
            }
        }
        return 0;
    }

    /**
     * getLocalInetAddress
     * @return
     */
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
            LOGGER.error("%s", e);
            throw new RuntimeException("get local inet address fail", e);
        }

        return inetAddressList;
    }

    /**
     * localhost
     * @return
     */
    public static String localhost() {
        List<String>  list = getLocalInetAddress();
        for (String i : list) {
            if (!LOCAL_EXCEPT_IP.contains(i) && i.indexOf(':') < 0) {
                return i;
            }
        }
        return "127.0.0.1";
    }
}
