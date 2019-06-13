package com.limite.jsrwebsocket.bridge;

import android.util.Log;
import android.webkit.JavascriptInterface;

import com.limite.jsrwebsocket.callback.JsCallBack;

/**
 * @author redli
 * @date 2019-04-22
 */
public class JsBridge {

    private String TAG = JsBridge.class.getSimpleName();
    private JsCallBack jsCallBack;
    private String IPV4;
    private String port;

    public JsBridge(JsCallBack jsCallBack, String IPV4, String port) {
        this.jsCallBack = jsCallBack;
        this.IPV4 = IPV4;
        this.port = port;
    }

    /**
     * 获取本地地址
     *
     * @return
     */
    @JavascriptInterface
    public String getIPV4() {
        return this.IPV4;
    }

    /**
     * 获取本地端口
     *
     * @return
     */
    @JavascriptInterface
    public String getPort() {
        return this.port;
    }


    /**
     * 播放
     */
    @JavascriptInterface
    public void play() {
        jsCallBack.start();
    }

    /**
     * 停止
     */
    @JavascriptInterface
    public void stop() {
        jsCallBack.stop();
    }

    /**
     * 上传文件
     */
    @JavascriptInterface
    public void uploadFiles() {
        jsCallBack.uploadFiles();
    }

    /**
     * 下载文件
     */
    @JavascriptInterface
    public void downFiles() {
        jsCallBack.downFiles();
    }

}
