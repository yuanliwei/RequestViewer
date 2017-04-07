package com.ylw.common.utils.download;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.IOUtils;
import com.ylw.common.utils.TaskUtils;

/**
 * @author 胡同涛
 * @description 下载管理类
 * @date 2015-3-24 下午2:14:56
 */
public class DownloadMgr {

	static DownloadMgr mgr = new DownloadMgr();
	private static Log log = LogFactory.getLog(DownloadMgr.class);

	public static void addDownloadInfo(DownloadInfo info) {
		TaskUtils.getExcutor().execute(new Runnable() {
			public void run() {
				mgr.download(info);
			}
		});
	}

	void download(DownloadInfo info) {
		// TODO Auto-generated method stub
		InputStream in = null;
		try {
			URL url2 = new URL(info.getUrl());
			HttpURLConnection connection = (HttpURLConnection) url2.openConnection();
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			connection.setConnectTimeout(20000);
			connection.connect();
			Map<String, List<String>> map = connection.getHeaderFields();
			JSONObject headers = new JSONObject();
			List<String> status = map.get(null);
			int code = Integer.parseInt(status.get(0).split(" ")[1]);
			if (code != 200) {
				log.warn(status.get(0) + " - " + info.getUrl());
				return;
			}
			for (String key : map.keySet()) {
				// log.debug(key + "--->" + map.get(key));
				headers.put(key, map.get(key));
			}
//			String type = map.get("Content-Type").get(0).split(";")[0];
			in = connection.getInputStream();
			saveInputStreamToFile(info.getSavePath(), in);
		} catch (Exception e) {
			log.error(info.getUrl(), e);
		} finally {
			IOUtils.close(in);
		}
	}

	private void saveInputStreamToFile(String path, InputStream in) {
		FileOutputStream os = null;
		try {
			os = new FileOutputStream(path);
			byte[] buffer = new byte[4096];
			int length = 0;
			while ((length = in.read(buffer)) > 0) {
				os.write(buffer, 0, length);
			}
			os.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.close(os);
		}
	}

}
