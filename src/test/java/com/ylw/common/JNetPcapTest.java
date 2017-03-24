package com.ylw.common;

import java.util.ArrayList;
import java.util.List;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;
import org.junit.Test;

public class JNetPcapTest {
	@Test
	public void testGetDeviceList() {
		List<PcapIf> arg0 = new ArrayList<>();

		StringBuilder arg1 = new StringBuilder();
		
		// 获取网卡列表
		Pcap.findAllDevs(arg0, arg1);

		arg0.forEach(action -> {
			System.out.println(action.getName());
			System.out.println(action);
			System.out.println(action.getDescription());
//			action.
//			action.getAddresses().forEach(a1 -> {
//				System.out.println(a1.getAddr().toString());
//			});
		});
		
//		Pcap.openLive(arg0, arg1, arg2, arg3, arg4)

		System.out.println(arg1);
	}
}
