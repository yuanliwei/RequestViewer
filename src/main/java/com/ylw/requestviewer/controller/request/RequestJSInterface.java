package com.ylw.requestviewer.controller.request;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSONObject;
import com.ylw.requestviewer.MainApp;

import javafx.application.Platform;
import netscape.javascript.JSObject;

public class RequestJSInterface {
	private static Log log = LogFactory.getLog(RequestJSInterface.class);

	public RequestJSInterface() {
	}

	public void start(String url, int deep) {
		System.out.println("start...............end");
	}

	public void testA() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				// JSObject jsObject = (JSObject)
				// mainApp.mainViewController.webEngine.executeScript("newJsObject()");
				// JSObject window = (JSObject)
				// mainApp.mainViewController.webEngine.executeScript("window");
				// jsObject.setMember("prefix", "pppp");
				// jsObject.setMember("body", "bbbbb");
				// jsObject.setMember("deep", "dddddd");
				// window.call("parseLinks", jsObject);
			}
		});
	}

	public void GET(JSObject params) throws Exception {
		URL url = new URL((String) params.getMember("url"));
		URLConnection connection = url.openConnection();
		connection.setRequestProperty("accept", "*/*");
		connection.setRequestProperty("connection", "Keep-Alive");
		connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
		// 建立实际的连接
		connection.connect();
		// 获取所有响应头字段
		Map<String, List<String>> map = connection.getHeaderFields();
		JSONObject headers = new JSONObject();
		// 遍历所有的响应头字段
		for (String key : map.keySet()) {
			System.out.println(key + "--->" + map.get(key));
			headers.put(key, map.get(key));
		}
		// 定义BufferedReader输入流来读取URL的响应
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		InputStream in = connection.getInputStream();
		byte[] buffer = new byte[4096];
		int length = 0;
		while ((length = in.read(buffer)) > 0) {
			outputStream.write(buffer, 0, length);
		}
		// System.out.println(new String(outputStream.toByteArray()));
		// callBack.call("alert", "ddddddddddd");
		// mainApp.mainViewController.exec(callBack+"()");
		params.call("onSuccess", headers.toJSONString(), new String(outputStream.toByteArray()));
		// params.call("onError",
		// "2CCCCCCCCCCCCCCCCCCCCCCCCGggggggggggggggggg");

	}

	public void POST(JSObject params) throws Exception {
		URL url = new URL((String) params.getMember("url"));
		URLConnection connection = url.openConnection();
		connection.setRequestProperty("accept", "*/*");
		connection.setRequestProperty("connection", "Keep-Alive");
		connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
		connection.setDoOutput(true);
		connection.setDoInput(true);
		// 获取URLConnection对象对应的输出流
		PrintWriter out = new PrintWriter(connection.getOutputStream());
		// 发送请求参数
		out.print(params.getMember("params"));
		// flush输出流的缓冲
		out.flush();
		// 获取所有响应头字段
		Map<String, List<String>> map = connection.getHeaderFields();
		// 遍历所有的响应头字段
		JSONObject headers = new JSONObject();
		for (String key : map.keySet()) {
			System.out.println(key + "--->" + map.get(key));
			headers.put(key, map.get(key));
		}
		// 定义BufferedReader输入流来读取URL的响应
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		InputStream in = connection.getInputStream();
		byte[] buffer = new byte[4096];
		int length = 0;
		while ((length = in.read(buffer)) > 0) {
			outputStream.write(buffer, 0, length);
		}
		params.call("onSuccess", headers.toJSONString(), new String(outputStream.toByteArray()));
		// params.call("onError",
		// "2CCCCCCCCCCCCCCCCCCCCCCCCGggggggggggggggggg");

	}
}
