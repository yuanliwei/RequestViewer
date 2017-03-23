package com.ylw.requestviewer.view;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ylw.requestviewer.controller.BaseController;

import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class WebPageController extends BaseController {

	private static Log log = LogFactory.getLog(WebPageController.class);

	@FXML
	WebView webView;

	WebEngine webEngine;

	@Override
	protected void initialize() {
		webEngine = webView.getEngine();
		webEngine.load("http://www.baidu.com");

		com.sun.javafx.webkit.WebConsoleListener.setDefaultListener(new com.sun.javafx.webkit.WebConsoleListener() {

			@Override
			public void messageAdded(WebView webView, String message, int lineNumber, String sourceId) {
				System.out.println("Console: " + message + " [" + sourceId + ":" + lineNumber + "] ");
			}
		});

	}

	public void load(String url) {
		log.debug("加载页面：" + url);
		if (url.startsWith("http")) {
			webEngine.load(url);
		} else {
			webEngine.load("file:///" + url);
		}
	}

}
