package jnetpcap.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import org.jnetpcap.protocol.tcpip.Http;
import org.jnetpcap.protocol.tcpip.Http.Request;
import org.jnetpcap.protocol.tcpip.Http.Response;
import org.jnetpcap.protocol.tcpip.Tcp;

import com.ylw.common.utils.ZipUtil;

public class HttpBuffer {

	private ByteArrayOutputStream bos = new ByteArrayOutputStream();
	private long startTime;
	private boolean handleData;
	private int httpPayLoadOffset;

	public HttpBuffer(Tcp tcp, Http http) {
		startTime = System.currentTimeMillis();
		try {
			bos.write(tcp.getPayload());
			checkData(http);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void checkData(Http http) {

		String host = http.fieldValue(Request.Host);
		String contentType = http.fieldValue(Response.Content_Type);
		System.out.println("host:" + host);
		System.out.println("ContentType:" + contentType);
		if (contentType == null || contentType.startsWith("text")) {
			handleData = true;
		} else {
			handleData = false;
		}
		httpPayLoadOffset = http.getPayloadOffset();
		// System.out.println(http.toDebugString());
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

	public String getString() {
		byte[] buffer = bos.toByteArray();
		buffer = handleGzip(buffer);
		return new String(buffer);
	}

	// TODO handleGzip ....
	private byte[] handleGzip(byte[] buffer) {
		if (!handleData) {
			return buffer;
		}
		if (buffer.length > httpPayLoadOffset + 10) {
			byte[] dest = new byte[buffer.length - httpPayLoadOffset];
			System.arraycopy(buffer, httpPayLoadOffset, dest, 0, dest.length);
			int ss = (dest[0] & 0xff) | ((dest[1] & 0xff) << 8);
			if (ss == GZIPInputStream.GZIP_MAGIC) {
				try {
					dest = ZipUtil.gunzip(dest);
					byte[] result = new byte[httpPayLoadOffset + dest.length];
					System.arraycopy(buffer, 0, result, 0, httpPayLoadOffset);
					System.arraycopy(dest, 0, result, httpPayLoadOffset, dest.length);
					return dest;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return buffer;
	}
}
