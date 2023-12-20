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
    private  static WebSocket wsConn=null;
    private static OkHttpClient wsClient;
    public static Boolean isAuthorized=false;
    private static WsService instace=null;
    private String cookies;
    public static WebSocket getSocket(){
        return wsConn;
    }
    private OkHttpClient client;

    public WsService() {
    }

    public static void closeWs(){
        if(wsClient!=null){
            wsClient.dispatcher().cancelAll();
        }
        wsConn=null;
        isAuthorized=false;
    }
    public static boolean startWS(){
        System.out.println("init strtws ............."+instace);

        if(instace!=null){
            instace.initWS();
            return true;
        }
        else{
            return false;
        }

    }

    public void initWS(){
        System.out.println("init ws .............");
        if(wsClient!=null && wsConn==null){

            Request request = new Request.Builder()
                    .url("ws://"+GlobData.serverAddress+":80")
                    .build();
            wsConn=wsClient.newWebSocket(request, wsListener);
        }

    }
    @Override
    public void onCreate() {

        wsClient=new OkHttpClient();
        this.initWS();
        instace=this;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        GlobData.getInstance(this).saveData(this);
    }
}