package com.ylw.requestviewer.controller.netcapture;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.util.TextUtils;

import com.ylw.common.utils.PropUtils;
import com.ylw.requestviewer.controller.BaseController;

import javafx.concurrent.Worker.State;
import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

public class NetCaptureController extends BaseController {

    private static Log log = LogFactory.getLog(NetCaptureController.class);

    @FXML
    WebView webView;

    WebEngine webEngine;

    @Override
    protected void initialize() {
        webEngine = webView.getEngine();
        load(PropUtils.get("html_NetCapture"));

        NetCaptureJSInterface jsObj = new NetCaptureJSInterface();

        webEngine.getLoadWorker().stateProperty().addListener((ov, oldState, newState) -> {
            JSObject window = (JSObject) webEngine.executeScript("window");
            System.out.println("newState  - " + newState + "   " + window.getMember("jsObj"));
            window.setMember("jsObj", jsObj);
            if (newState == State.SUCCEEDED) {
                // exec("onPageLoaded()");
            }
        });

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
        } else if (url.startsWith("http") || url.startsWith("file:")) {
            webEngine.load(url);
        } else {
            webEngine.load("file:///" + url);
        }
    }

    public void exec(String jsData) {
        log.debug("exec : " + jsData);
        webEngine.executeScript(jsData);
    }

}
