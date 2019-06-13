package com.limite.jsrwebsocket.base;

import android.app.Application;
import android.util.Log;

import com.limite.jsrwebsocket.server.WebServer;

import org.java_websocket.WebSocket;

import java.util.Collection;
import java.util.Iterator;

/**
 * @author redli
 * @date 2019-04-28
 */
public class App extends Application {

    private WebServer webServer;
    private String TAG = App.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        initJavaWebSocket();
    }


    private void initJavaWebSocket() {
        webServer = WebServer.getInstance();
        webServer.setReuseAddr(true);
        webServer.start();
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.i(TAG, "onTerminate: ");
        stopServer();
    }

    private void stopServer() {
        if (webServer != null) {
            try {
                Collection<WebSocket> webSockets = WebServer.getInstance().getConnections();
                Iterator iterator = webSockets.iterator();
                while (iterator.hasNext()) {
                    ((WebSocket)iterator.next()).close();
                }
                webServer.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
