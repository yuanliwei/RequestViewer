package com.ylw.requestviewer.pcap;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.tcpip.Http;
import org.jnetpcap.protocol.tcpip.Tcp;

import com.ylw.requestviewer.MainApp;

public abstract class HttpHandler<T> implements PcapPacketHandler<T> {

    private static Log log = LogFactory.getLog(HttpHandler.class);

    Http http = new Http();
    Tcp tcp = new Tcp();
    Ip4 ip4 = new Ip4();

    Map<String, HttpBuffer> bufferMap = new HashMap<>();

    @Override
    public void nextPacket(PcapPacket packet, T user) {
        if (packet.hasHeader(ip4)) {
            if (packet.hasHeader(tcp)) {
                if (packet.hasHeader(http)) {
                    byte[] address = new byte[4];
                    ip4.sourceToByteArray(address);
                    String key = asString(address) + ":" + tcp.source();
                    ip4.destinationToByteArray(address);
                    String friendKey = asString(address) + ":" + tcp.destination();
                    HttpBuffer buffer = bufferMap.get(key);
                    if (buffer == null) {
                        buffer = new HttpBuffer(tcp, http);
                        buffer.setKeyAndFriendKey(key, friendKey);
                        bufferMap.put(key, buffer);
                    } else {
                        buffer.addSegment(tcp);
                    }
                } else {
                    byte[] address = new byte[4];
                    ip4.sourceToByteArray(address);
                    String key = asString(address) + ":" + tcp.source();
                    HttpBuffer buffer = bufferMap.get(key);
                    if (buffer != null) {
                        buffer.addSegment(tcp);
                        if (tcp.flags_FIN()) {
                            HttpBuffer buffer2 = bufferMap.remove(buffer.getFriendKey());
                            log.debug("begin FK : " + buffer.getFriendKey());
                            if (buffer2 == null) {
                                nextHttpPacket(buffer.getHeader(), buffer.getContent(), user);
                            } else {
                                if (buffer.isResponse()) {
                                    nextHttpPacket(buffer2.getHeader(), buffer2.getContent(), user);
                                    nextHttpPacket(buffer.getHeader(), buffer.getContent(), user);
                                } else {
                                    nextHttpPacket(buffer.getHeader(), buffer.getContent(), user);
                                    nextHttpPacket(buffer2.getHeader(), buffer2.getContent(), user);
                                }
                            }
                            log.debug("end FK : " + buffer.getFriendKey());
                            bufferMap.remove(key);
                        }
                    }
                }
            }
        }
    }

    protected abstract void nextHttpPacket(String header, String content, T user);

    private static String asString(final byte[] mac) {
        final StringBuilder buf = new StringBuilder();
        for (byte b : mac) {
            if (buf.length() != 0) {
                buf.append(':');
            }
            buf.append(b & 0xff);
        }

        return buf.toString();
    }
}
