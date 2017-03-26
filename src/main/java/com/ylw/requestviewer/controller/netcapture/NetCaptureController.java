package com.ylw.requestviewer.controller.netcapture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.util.TextUtils;

import com.ylw.requestviewer.controller.BaseController;

import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class NetCaptureController extends BaseController {

	private static Log log = LogFactory.getLog(NetCaptureController.class);

	@FXML
	WebView webView;

	WebEngine webEngine;

	@Override
	protected void initialize() {
		webEngine = webView.getEngine();
		webEngine.load("http://www.guokr.com");

		com.sun.javafx.webkit.WebConsoleListener.setDefaultListener(new com.sun.javafx.webkit.WebConsoleListener() {

			@Override
			public void messageAdded(WebView webView, String message, int lineNumber, String sourceId) {
				System.out.println("Console: " + message + " [" + sourceId + ":" + lineNumber + "] ");
			}
		});

	}

	public void load(String url) {
		log.debug("加载页面：" + url);
		if (TextUtils.isBlank(url)) {
			webEngine.load("http://www.baidu.com");
		} else if (url.startsWith("http")) {
			webEngine.load(url);
		} else {
			webEngine.load("file:///" + url);
		}
	}

}