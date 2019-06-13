package com.limite.jsrwebsocket.server;

import android.util.Log;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

/**
 * @author redli
 * @date 2019-04-25
 */
public class WebServer extends WebSocketServer {

    private String TAG = WebServer.class.getSimpleName();

    private WebServer() {
        super(new InetSocketAddress(12365));
    }

    private static class SingletonInstance {
        private static final WebServer INSTANCE = new WebServer();
    }

    public static WebServer getInstance() {
        return SingletonInstance.INSTANCE;
    }

    public WebSocket client;

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        //This method sends a message to the new client
//        conn.send("Welcome to the server!");
        //This method sends a message to all clients connected
//        broadcast("new connection: " + handshake.getResourceDescriptor());
        client = conn;
        Log.i(TAG, "onOpen: " + conn.getRemoteSocketAddress().getAddress().getHostAddress() + " entered the room!");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        Log.i(TAG, "onClose: " + conn.getResourceDescriptor() + " has left the room!");
        broadcast(conn + " has left the room!");
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        broadcast(message);
        Log.i(TAG, "onMessage: " + conn.getResourceDescriptor() + ": " + message);
    }

    @Override
    public void onMessage(WebSocket conn, ByteBuffer message) {
        broadcast(message.array());
        Log.i(TAG, "onMessage: " + conn.getResourceDescriptor() + ": " + message);
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        Log.i(TAG, "onError: " + "");
        ex.printStackTrace();
        if (conn != null) {
            // some errors like port binding failed may not be assignable to a specific websocket
            conn.close();
        }
    }

    @Override
    public void onStart() {
        Log.i(TAG, "onStart: " + "Server started!");
        setConnectionLostTimeout(0);
        setConnectionLostTimeout(100);
    }
}
