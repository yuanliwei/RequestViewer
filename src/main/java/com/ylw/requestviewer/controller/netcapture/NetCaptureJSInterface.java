package com.ylw.requestviewer.controller.netcapture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ylw.requestviewer.MainApp;

import javafx.application.Platform;
import netscape.javascript.JSObject;

public class NetCaptureJSInterface {
	private static Log log = LogFactory.getLog(NetCaptureJSInterface.class);
	private MainApp mainApp;

	public NetCaptureJSInterface(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	public void start(String url, int deep) {
		System.out.println("start...............end");
	}

	public void testA() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
//				JSObject jsObject = (JSObject) mainApp.mainViewController.webEngine.executeScript("newJsObject()");
//				JSObject window = (JSObject) mainApp.mainViewController.webEngine.executeScript("window");
//				jsObject.setMember("prefix", "pppp");
//				jsObject.setMember("body", "bbbbb");
//				jsObject.setMember("deep", "dddddd");
//				window.call("parseLinks", jsObject);
			}
		});
	}

	public void POST(JSObject params) throws Exception {

	}

	public void setMainApp(MainApp mainApp2) {
		this.mainApp = mainApp2;
	}

	public void testB() {
//		new EPUBMain().start();
	}
}