package jnetpcap;

import org.jnetpcap.packet.JMemoryPacket;
import org.jnetpcap.packet.JPacket;
import org.jnetpcap.protocol.JProtocol;
import org.junit.Test;

/**
 * @author Mark Bednarczyk
 * @author Sly Technologies, Inc.
 */
public class CreateInMemoryPacketExample {

	/**
	 * @param args
	 */
	@Test
	public void main() {

		JPacket packet = new JMemoryPacket(JProtocol.ETHERNET_ID,
				/*
				 * Data acquired using JMemory.toHexdump on a different packet
				 */
				"      16037801 16030060 089fb1f3 080045c0" + "01d4e253 0000ff01 ae968397 20158397"
						+ "013b0303 27310000 00004500 01b8cb91" + "4000fe11 87248397 013b8397 20151b5b"
						+ "070001a4 ae1e382b 3948e09d bee80000" + "00010000 00010000 00020106 00000000"
						+ "00340000 00720000 006f0000 006f0000" + "00740000 002e0000 00630000 00650000");

		System.out.println(packet.toString());
	}

}
