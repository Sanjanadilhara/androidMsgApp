package com.example.msgapplication;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.msgapplication.models.Conversation;

import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class WsService extends Service {

    private  WebSocketListener wsListener=new WebSocketListener() {
        @Override
        public void onClosed(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
            super.onClosed(webSocket, code, reason);
        }

        @Override
        public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable t, @Nullable Response response) {
            System.out.println("ws fail");
            Log.e("System.out", "WebSocket failure: " + t.getMessage());
        }

        @Override
        public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
            System.out.println(text);
            try {
                WsMessageHandler.handleIncommingMessages(new JSONObject(text), getApplicationContext());
                System.out.println("parse ok");
            }catch (Exception e){
                System.out.println("fail to parse json");
            }

        }

        @Override
        public void onOpen(@NonNull WebSocket webSocket, @NonNull Response response) {
            WsMessageHandler.initializeConnection(getApplicationContext());
            System.out.println("ws opened");
        }
    };
    private  static WebSocket wsConn;
    private static Handler wsHandler;
    private static WsService instace;
    private String cookies;
    public static Handler getHandler(){return wsHandler;}
    public static WebSocket getSocket(){
        return wsConn;
    }
    private OkHttpClient client;

    public WsService() {
    }

    public static void startWS(){
        instace.initWS();
    }

    public void initWS(){

        Request request = new Request.Builder()
                .url("ws://192.168.171.21:80")
                .build();
        wsHandler.post(new Runnable() {
            @Override
            public void run() {
                System.out.println("in thread");
                wsConn=client.newWebSocket(request, wsListener);

            }
        });

    }
    @Override
    public void onCreate() {

        client=new OkHttpClient();

        HandlerThread thread = new HandlerThread("msg-websocket");
        thread.start();
        wsHandler=new Handler(thread.getLooper());
        System.out.println("in service");

        this.initWS();


    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



}