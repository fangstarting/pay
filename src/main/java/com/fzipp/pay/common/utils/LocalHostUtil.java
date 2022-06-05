package com.fzipp.pay.common.utils;

import com.fzipp.pay.common.config.Info;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * 本地主机工具类
 *
 * @author fengfang
 * @since 2022年05月17日04:52:10
 */
public class LocalHostUtil {


    /**
     * 获取主机名称
     *
     * @return
     * @throws UnknownHostException
     */
    public static String getHostName() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostName();
    }

    /**
     * 获取系统首选IP //TODO 生产环境IP为公网IP
     *
     * @return
     * @throws UnknownHostException
     */
    public static String getLocalIP() throws UnknownHostException {
        //TODO 生产环境IP存在则输出公网IP否则输出本机IP
        if (Info.getNetworkIP()==null||"".equals(Info.getNetworkIP())){
            return InetAddress.getLocalHost().getHostAddress();
        }else{
            return Info.getNetworkIP();
        }
    }

    /**
     * 获取所有网卡IP，排除回文地址、虚拟地址
     *
     * @return
     * @throws SocketException
     */
    public static String[] getLocalIPs() throws SocketException {
        List<String> list = new ArrayList<>();
        Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
        while (enumeration.hasMoreElements()) {
            NetworkInterface intf = enumeration.nextElement();
            if (intf.isLoopback() || intf.isVirtual()) { //
                continue;
            }
            Enumeration<InetAddress> inets = intf.getInetAddresses();
            while (inets.hasMoreElements()) {
                InetAddress addr = inets.nextElement();
                if (addr.isLoopbackAddress() || !addr.isSiteLocalAddress() || addr.isAnyLocalAddress()) {
                    continue;
                }
                list.add(addr.getHostAddress());
            }
        }
        return list.toArray(new String[0]);
    }

    /**
     * 判断操作系统是否是Windows
     *
     * @return
     */
    public static boolean isWindowsOS() {
        boolean isWindowsOS = false;
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().indexOf("windows") > -1) {
            isWindowsOS = true;
        }
        return isWindowsOS;
    }

    public static void main(String[] args) {
        try {
            System.out.println("主机是否为Windows系统：" + LocalHostUtil.isWindowsOS());
            System.out.println("主机名称：" + LocalHostUtil.getHostName());
            System.out.println("系统首选IP：" + LocalHostUtil.getLocalIP());
            System.out.println("系统所有IP：" + String.join(",", LocalHostUtil.getLocalIPs()));
        } catch (UnknownHostException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
