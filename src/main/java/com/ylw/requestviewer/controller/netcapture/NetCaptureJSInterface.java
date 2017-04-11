package com.ylw.requestviewer.controller.netcapture;

import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jnetpcap.PcapIf;

import com.alibaba.fastjson.JSON;
import com.ylw.common.utils.event.EventUtil;
import com.ylw.requestviewer.controller.BaseJSInterface;
import com.ylw.requestviewer.model.StopAppEvent;
import com.ylw.requestviewer.pcap.HttpPcap;

import javafx.application.Platform;
import netscape.javascript.JSObject;

public class NetCaptureJSInterface extends BaseJSInterface {

    private static Log log = LogFactory.getLog(NetCaptureJSInterface.class);

    private HttpPcap pcap;

    public NetCaptureJSInterface() {
        pcap = new HttpPcap();
        EventUtil.register(this);
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

    public void POST(JSObject params) throws Exception {

    }

    public void testB() {
        // new EPUBMain().start();
    }

    public String findAllDevs() {
        List<PcapIf> devs = pcap.findAllDevs();
        if (devs == null) {
            return "[]";
        }
        return JSON.toJSONString(devs);
    }

    public boolean startCapture(String devName) {
        try {
            pcap.startCapture(devName);
            return true;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    public boolean stopCapture() {
        pcap.stopCapture();
        return true;
    }

    public void registerCaptureCallback(JSObject callback) {
        pcap.setCaptureListener(new HttpPcap.ICaptureListener() {

            @Override
            public void onHttpPacket(String header, String content) {
//                 log.debug(header + content);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        callback.call("onHttpPacket", header, content);
                    }
                });
            }
        });
    }

    public void onEvent(StopAppEvent e) {
        stopCapture();
    }
}
