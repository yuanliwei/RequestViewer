package jnetpcap.http;

import java.util.HashMap;
import java.util.Map;

import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.tcpip.Http;
import org.jnetpcap.protocol.tcpip.Tcp;

public abstract class HttpHandler<T> implements PcapPacketHandler<T> {

	Http http = new Http();
	Tcp tcp = new Tcp();
	Ip4 ip4 = new Ip4();

	Map<String, HttpBuffer> bufferMap = new HashMap<>();

	@Override
	public void nextPacket(PcapPacket packet, T user) {
		if (packet.hasHeader(ip4)) {
			if (packet.hasHeader(tcp)) {
				// String un = tcp.source() + "|" + tcp.destination() + "|" +
				// ip4.sourceToInt() + "|"
				// + ip4.destinationToInt();
				// System.out.println("UN:" + un + " seq:" + tcp.seq() + "
				// pSize:"
				// + tcp.getPayloadLength() + " ack:" + tcp.ack() + " flag:" +
				// tcp.flagsCompactString());
				if (packet.hasHeader(http)) {
					System.out.println(http.contentType());
					String s1 = http.getUTF8String(0, http.getHeaderLength());
					byte[] address = new byte[4];
					ip4.sourceToByteArray(address);
					String key = asString(address) + ":" + tcp.source();
					HttpBuffer buffer = bufferMap.get(key);
					if (buffer == null) {
						buffer = new HttpBuffer(tcp, http);
						bufferMap.put(key, buffer);
						System.out.println("new key:" + key);
					} else {
						buffer.addSegment(tcp);
						System.out.println("add key:" + key);
					}
				} else {
					byte[] address = new byte[4];
					ip4.sourceToByteArray(address);
					String key = asString(address) + ":" + tcp.source();
					HttpBuffer buffer = bufferMap.get(key);
					if (buffer != null) {
						buffer.addSegment(tcp);
						System.out.println("add tcp key:" + key);
						if (tcp.flags_FIN()) {
							System.out.println("over tcp key:" + key);
							nextHttpPacket(buffer, user);
						}
					}
				}
			}
		}
	}

	protected abstract void nextHttpPacket(HttpBuffer buffer, T user);

	private static String asString(final byte[] mac) {
		final StringBuilder buf = new StringBuilder();
		for (byte b : mac) {
			if (buf.length() != 0) {
				buf.append(':');
			}
			// if (b >= 0 && b < 16) {
			// buf.append('0');
			// }
			buf.append(b & 0xff);
			// buf.append(Integer.toHexString((b < 0) ? b + 256 :
			// b).toUpperCase());
		}

		return buf.toString();
	}
}
