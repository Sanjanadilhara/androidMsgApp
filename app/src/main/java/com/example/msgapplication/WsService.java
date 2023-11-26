package com.example.msgapplication;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class WsService extends Service {

    private  static WebSocket wsConn;
    private static Handler wsHandler;
    private String cookies;
    public static Handler getHandler(){
        return wsHandler;
    }
    public static WebSocket getSocket(){
        return wsConn;
    }
    private OkHttpClient client;
    Context c;

    public WsService() {
    }

    @Override
    public void onCreate() {

        c=getApplicationContext();
        SharedPreferences pref=this.getSharedPreferences("cookieStore", MODE_PRIVATE);
        cookies=pref.getString("cookies", "");
        client=new OkHttpClient();
        Request request = new Request.Builder()
                .url("ws://13.235.95.222:80")
                .build();

        HandlerThread thread = new HandlerThread("msg-websocket");
        thread.start();

        wsHandler=new Handler(thread.getLooper());
        System.out.println("in service");
        wsHandler.post(new Runnable() {
            @Override
            public void run() {
                System.out.println("in thread");
                wsConn=client.newWebSocket(request, new WebSocketListener() {
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
                            handleMessages(new JSONObject(text));
                            System.out.println("parse ok");
                        }catch (Exception e){
                            System.out.println("fail to parse json");
                        }

                    }

                    @Override
                    public void onOpen(@NonNull WebSocket webSocket, @NonNull Response response) {
                        webSocket.send("{\"type\":\"init\", \"cookie\":\""+cookies+"\"}");
                        System.out.println("ws opened");
                    }
                });

            }
        });

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void handleMessages(JSONObject data){
        try {
            switch (data.getString("type")){
                case "message":
                    int index=-1;
                    for (int y=0; y<Conversations.getInstance(this).conversations.size(); y++){
                        if(Conversations.getInstance(this).conversations.get(y).equals(data.getString("from"))){
                            index=y;
                        }
                    }
                    System.out.println("message get index: "+index+"\nfrom: "+data.getString("from")+"\nconsid: "+Conversations.getInstance(this).conversations.get(index).hashCode()+"hash:"+data.getString("from").hashCode());
                    Conversations.getInstance(this).conversations.get(1).lastMessages.add(new Conversation.Message(data.getString("message"), false));

                    Conversations.getInstance(this).events.forEach(eventListener -> {
                        eventListener.onUpdate();
                    });

                    break;
                case "messageStat":
                    break;
            }
        }catch (Exception e){

        }

    }
}