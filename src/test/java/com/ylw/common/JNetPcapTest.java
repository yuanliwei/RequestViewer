package com.ylw.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapAddr;
import org.jnetpcap.PcapIf;
import org.jnetpcap.PcapSockAddr;
import org.jnetpcap.winpcap.WinPcap;
import org.junit.Test;

import com.ylw.common.utils.NativeUtils;
import com.ylw.requestviewer.MainApp;

public class JNetPcapTest {

    private static Log log = LogFactory.getLog(JNetPcapTest.class);

    @Test
    public void testGetDeviceList() {
        List<PcapIf> arg0 = new ArrayList<>();

        StringBuilder arg1 = new StringBuilder();

        try {
            NativeUtils.loadLibraryFromJar("/native/windows/x86_64/" + System.mapLibraryName("jnetpcap"));
//            NativeUtils.loadLibraryFromJar("/native/windows/x86_64/" + System.mapLibraryName("jnetpcap-pcap100"));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        // 获取网卡列表
        Pcap.findAllDevs(arg0, arg1);

        String deviceName = null;
        for (PcapIf pcapIf : arg0) {
            System.out.println(pcapIf.getName());
            System.out.println(pcapIf);
            System.out.println(pcapIf.getDescription());
            // action.
            List<PcapAddr> addrs = pcapIf.getAddresses();
            for (PcapAddr paddr : addrs) {
                PcapSockAddr addr = paddr.getAddr();
                System.out.println("addr family : " + addr.getFamily());
                System.out.println("addr family4 : " + PcapSockAddr.AF_INET);
                System.out.println("addr family6 : " + PcapSockAddr.AF_INET6);
                if (addr.getFamily() == PcapSockAddr.AF_INET) {
                    deviceName = pcapIf.getName();
                }
            }
        }
        // http://blog.sina.com.cn/s/blog_40d608bb01010nt3.html

        // 打开链接
        // Pcap.openLive(device.getName(), snaplen, flags, timeout, errbuf)
        int snaplen;// 每次捕捉的数据量
        int flags; // 捕捉方式
        int timeout;// 超时
        StringBuilder errbuf;// 错误信息缓冲
        // Pcap pcap = Pcap.openLive(deviceName, snaplen, flags, timeout,
        // errbuf);
        // Pcap.openLive(arg0, arg1, arg2, arg3, arg4)

        // 开始监听
        // pcap.loop(int cnt, JPacketHandler<T> handler, T user)
        System.out.println(arg1);

        System.out.println(WinPcap.isSupported());
    }
}
