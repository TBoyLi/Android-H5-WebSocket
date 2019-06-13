package com.limite.jsrwebsocket.callback;

/**
 * @author redli
 * @date 2019-04-22
 */
public interface JsCallBack {

    /**
     * 开启连接
     */
    void start();


    /**
     * 关闭连接
     */
    void stop();


    /**
     * 上传文件
     */
    void uploadFiles();

    /**
     * 下载文件
     */
    void downFiles();

}
