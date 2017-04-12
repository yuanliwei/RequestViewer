package com.ylw.requestviewer.pcap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.util.TextUtils;
import org.jnetpcap.protocol.tcpip.Http;
import org.jnetpcap.protocol.tcpip.Http.Response;
import org.jnetpcap.protocol.tcpip.Tcp;

import com.ylw.common.utils.ZipUtil;

public class HttpBuffer {

    private static Log log = LogFactory.getLog(HttpBuffer.class);

    private ByteArrayOutputStream bos = new ByteArrayOutputStream();
    private boolean handleData;
    private boolean hasChunk;
    private boolean isResponse;
    private String header;
    private String key;
    private String friendKey;

    private long baseSeq;

    public HttpBuffer(Tcp tcp, Http http) {
        try {
            header = new String(http.getHeader());
            checkData(http);
            if (handleData) {
                bos.write(http.getPayload());
            }
            if (key == null) {
                baseSeq = tcp.seq();
            }
            log.debug("--------------------FK-- " + key + " SEQ:" + (tcp.seq() - baseSeq));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkData(Http http) {

        isResponse = http.isResponse();
        String contentType = http.fieldValue(Response.Content_Type);
        if (http.hasAVP("Transfer-Encoding")) {
            String chunk = http.getAVP("Transfer-Encoding");
            if ("chunked".equals(chunk)) {
                hasChunk = true;
            }
        }
        // System.out.println("host:" + host);
        // System.out.println("ContentType:" + contentType);

        if (contentType == null || contentType.startsWith("text") || contentType.startsWith("application/json")) {
            handleData = true;
        } else {
            handleData = false;
        }
        if (!isResponse && http.hasContent()) {
            handleData = true;
        }
    }

    List<SeqTcpData> tcps = new ArrayList<>();

    public void addSegment(Tcp tcp) {
        if (!handleData) {
            return;
        }
        tcps.add(new SeqTcpData(tcp));
    }

    private byte[] getChunkContent(byte[] content) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(content);

            do {
                String chunk = readShortLine(bis);
                do {
                    if (TextUtils.isBlank(chunk)) {
                        chunk = readShortLine(bis);
                    } else {
                        break;
                    }
                } while (bis.available() > 0);
                if (chunk == null)
                    break;
                int length = Integer.valueOf(chunk, 16);
                if (length <= 0)
                    break;
                length = Math.min(length, bis.available());
                byte[] buffer = new byte[length];
                bis.read(buffer, 0, length);
                bos.write(buffer, 0, length);
            } while (true);
        } catch (IOException e) {
            e.printStackTrace();
            return content;
        }
        return bos.toByteArray();
    }

    private String readShortLine(ByteArrayInputStream bis) throws IOException {
        bis.mark(0);
        byte[] b = new byte[50];
        bis.read(b);
        for (int i = 0; i < b.length - 1; i++) {
            if (b[i] == '\r' && b[i + 1] == '\n') {
                bis.reset();
                int len = bis.read(b, 0, i + 2);
                return new String(b, 0, len).trim();
            }
        }
        bis.reset();
        bis.read();
        return null;
    }

    // handleGzip ....
    private byte[] handleGzip(byte[] buffer) {
        if (!handleData) {
            return buffer;
        }
        if (buffer.length > 2) {
            int ss = (buffer[0] & 0xff) | ((buffer[1] & 0xff) << 8);
            if (ss == GZIPInputStream.GZIP_MAGIC) {
                try {
                    byte[] dest = ZipUtil.gunzip(buffer);
                    // String string = Hex.bytesToHexString(buffer);
                    // System.out.println(string);
                    return dest;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return buffer;
    }

    public String getContent() {
        // if size > 1M
        assembledTcpData();
        if (bos.size() > 1 * 1024 * 1024) {
            return "too long content.....";
        }
        byte[] content = bos.toByteArray();
        if (hasChunk) {
            content = getChunkContent(content);
        }
        byte[] buffer = handleGzip(content);
        return new String(buffer);
    }

    private void assembledTcpData() {
        // TODO Auto-generated method stub
        Collections.sort(tcps, new Comparator<SeqTcpData>() {

            @Override
            public int compare(SeqTcpData o1, SeqTcpData o2) {
                // TODO Auto-generated method stub
                if (o1 == null && o2 == null) {
                    return 0;
                }
                if (o1 == null) {
                    return -1;
                }
                if (o2 == null) {
                    return 1;
                }
                if (o1.seq > o2.seq) {
                    return 1;
                }
                if (o2.seq > o1.seq) {
                    return -1;
                }
                return 0;
            }
        });
        for (SeqTcpData tcp : tcps) {
            // log.debug("-----------3333---------FK-- " + key + " SEQ:" +
            // (tcp.seq - baseSeq));
            byte[] data = tcp.data;
            try {
                bos.write(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class SeqTcpData {
        long seq;
        byte[] data;

        public SeqTcpData(Tcp tcp) {
            seq = tcp.seq();
            data = tcp.getPayload();
        }
    }

    public String getHeader() {
        return header;
    }

    public void setKeyAndFriendKey(String key, String friendKey) {
        // TODO Auto-generated method stub
        this.key = key;
        this.friendKey = friendKey;
    }

    public String getFriendKey() {
        return friendKey;
    }

    public boolean isResponse() {
        return isResponse;
    }
}
