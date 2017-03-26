package com.ylw.requestviewer.pcap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import org.apache.http.util.TextUtils;
import org.jnetpcap.protocol.tcpip.Http;
import org.jnetpcap.protocol.tcpip.Http.Response;
import org.jnetpcap.protocol.tcpip.Tcp;

import com.ylw.common.utils.ZipUtil;

public class HttpBuffer {

	private ByteArrayOutputStream bos = new ByteArrayOutputStream();
	private long startTime;
	private boolean handleData;
	private boolean hasChunk;
	private boolean isResponse;
	private String header;
	private String key;
	private String friendKey;

	public HttpBuffer(Tcp tcp, Http http) {
		startTime = System.currentTimeMillis();
		try {
			bos.write(http.getPayload());
			header = new String(http.getHeader());
			checkData(http);
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

	public void addSegment(Tcp tcp) {
		if (!handleData) {
			return;
		}
		byte[] data = tcp.getPayload();
		try {
			bos.write(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
