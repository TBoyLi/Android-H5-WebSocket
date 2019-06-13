package com.limite.jsrwebsocket.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.just.agentweb.AgentWeb;
import com.limite.jsrwebsocket.R;
import com.limite.jsrwebsocket.bridge.JsBridge;
import com.limite.jsrwebsocket.callback.JsCallBack;
import com.limite.jsrwebsocket.server.WebServer;

import org.java_websocket.WebSocket;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * @author redli
 */
public class MainActivity extends AppCompatActivity implements JsCallBack, EasyPermissions.PermissionCallbacks {

    private String TAG = MainActivity.class.getSimpleName();

    private AgentWeb mAgentWeb;

    private TextView mTvIp;

    private LinearLayout container;

    private String localIp;

    private static final int port = 12365;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {

        } else {
            EasyPermissions.requestPermissions(this, "需要权限",
                    1, perms);
        }
        initWebView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        stopSocket();
    }

    private void stopSocket() {
        try {
            Collection<WebSocket> webSockets = WebServer.getInstance().getConnections();
            Iterator iterator = webSockets.iterator();
            while (iterator.hasNext()) {
                ((WebSocket) iterator.next()).close();
            }
            WebServer.getInstance().stop();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        localIp = NetworkUtils.getIPAddress(true);
        mTvIp = findViewById(R.id.ip);
        mTvIp.setText(localIp);

        container = findViewById(R.id.container);

        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(container, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .createAgentWeb()
                .ready()
                .go("file:///android_asset/video.html");
//                .go("http://10.255.1.16/video/video.html");
        mAgentWeb.clearWebCache();
        mAgentWeb.getJsInterfaceHolder().addJavaObject("NativeBridge", new JsBridge(this, localIp, "" + port));
    }

    @Override
    public void start() {
        final WebSocket client = WebServer.getInstance().client;
        final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    long startTimeMills = TimeUtils.getNowMills();
                    int readLength = 1024 * 64;
                    RandomAccessFile randomFile = new RandomAccessFile("/storage/emulated/0/Movies/alita-1G1.mp4", "r");
                    long totalSize = randomFile.length();
                    byte[] block;
                    while (totalSize > 0) {
                        block = new byte[readLength];
                        randomFile.read(block);
                        //OutOfMemoryError
                        client.send(block);
                        block = null;
                        totalSize -= readLength;
                    }
                    if (randomFile != null) {
                        randomFile.close();
                    }
                    long endTimeMills = TimeUtils.getNowMills();
                    Log.d("Time: ", "" + (endTimeMills - startTimeMills));
                    WebServer.getInstance().broadcast("exit");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void stop() {
        stopSocket();
    }

    @Override
    public void uploadFiles() {

    }

    @Override
    public void downFiles() {

    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }
}
