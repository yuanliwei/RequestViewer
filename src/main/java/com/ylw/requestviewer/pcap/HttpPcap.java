package com.ylw.requestviewer.pcap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jnetpcap.Pcap;
import org.jnetpcap.PcapIf;

import com.ylw.common.utils.TaskUtils;

public class HttpPcap {
	Pcap pcap;

	public List<PcapIf> findAllDevs() {
		List<PcapIf> alldevs = new ArrayList<PcapIf>(); // Will be filled with
		// NICs
		StringBuilder errbuf = new StringBuilder(); // For any error msgs

		/***************************************************************************
		 * First get a list of devices on this system
		 **************************************************************************/
		int r = Pcap.findAllDevs(alldevs, errbuf);
		if (r == Pcap.NOT_OK || alldevs.isEmpty()) {
			System.err.printf("Can't read list of devices, error is %s", errbuf.toString());
			return null;
		}

		System.out.println("Network devices found:");
		return alldevs;
	}

	public void startCapture(String devName) throws IOException {
		int snaplen = 64 * 1024; // Capture all packets, no trucation
		int flags = Pcap.MODE_PROMISCUOUS; // capture all packets
		int timeout = 10 * 1000; // 10 seconds in millis
		StringBuilder errbuf = new StringBuilder();
		pcap = Pcap.openLive(devName, snaplen, flags, timeout, errbuf);

		if (pcap == null) {
			System.err.printf("Error while opening device for capture: " + errbuf.toString());
			throw new IOException("Error while opening device for capture: " + errbuf.toString());
		}

		HttpHandler<String> httpHandler = new HttpHandler<String>() {
			@Override
			protected void nextHttpPacket(String header, String content, String user) {
				// System.out.println(header + content);
				if (captureListener != null) {
					captureListener.onHttpPacket(header, content);
				}
			}
		};

		TaskUtils.getSingleExcutor().execute(new Runnable() {
			
			@Override
			public void run() {
				pcap.loop(-1, httpHandler, "jNetPcap rocks!");
			}
		});
	}

	public void stopCapture() {
		if (pcap != null) {
			pcap.close();
		}
	}

	public interface ICaptureListener {
		void onHttpPacket(String header, String content);
	}

	ICaptureListener captureListener;

	public void setCaptureListener(ICaptureListener captureListener) {
		this.captureListener = captureListener;
	}
}
