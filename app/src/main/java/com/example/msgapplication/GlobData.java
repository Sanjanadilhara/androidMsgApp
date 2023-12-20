package com.example.msgapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Shader;
import android.net.ConnectivityManager;
import android.net.Network;

import androidx.annotation.NonNull;

import com.example.msgapplication.helpers.FileHandler;
import com.example.msgapplication.models.ConRequests;
import com.example.msgapplication.models.Conversation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class GlobData extends ConnectivityManager.NetworkCallback {
    @Override
    public void onAvailable(@NonNull Network network) {
        if(!WsService.startWS()){
            System.out.println("strtService.......");
            context.startService(new Intent(context, WsService.class));
        }
        this.updateViews(true);
    }

    @Override
    public void onLost(@NonNull Network network) {
        WsService.closeWs();
        this.updateViews(false);
    }

    public static String serverAddress="192.168.142.21";

    public ArrayList<EventListener> events=new ArrayList<>();
    public ArrayList<NetStaEventListener> networkStatEvents=new ArrayList<>();
    public static interface NetStaEventListener{
        public void onUpdate(boolean networkAvailable);
    }  public static interface EventListener{
        public void onUpdate();
    }
    public void updateViews(){
        events.forEach(new Consumer<EventListener>() {
            @Override
            public void accept(EventListener eventListener) {
                try{
                    eventListener.onUpdate();
                }catch (Exception e){

                }
            }
        });
    }
    public void setUpdateListener(EventListener e){
        events.add(e);
    }
    public void updateViews(boolean netStat){
        networkStatEvents.forEach(new Consumer<NetStaEventListener>() {
            @Override
            public void accept(NetStaEventListener netStaEventListener) {
                try{
                netStaEventListener.onUpdate(netStat);
                }catch (Exception e){

                }
            }
        });
    }
    public void setNetStatUpdateListener(NetStaEventListener e){
        networkStatEvents.add(e);
    }
    private static GlobData staticCons;
    public LinkedList<Conversation> conversations;
    public ConRequests requests;
    private Context context;
    private  String conFile="conversations";

    public static synchronized GlobData getInstance(Context ctx){
        if(staticCons == null){
            staticCons=new GlobData(ctx);
            System.out.println("null here");
        }
        return staticCons;
    }
    GlobData(Context context){
        System.out.println("initilizing conversatiosns");
        conversations=new LinkedList<>();
        FileHandler.readFile(context, conFile, new FileHandler.Content() {
            @Override
            public void read(String line) {
                System.out.println("this is a line:"+line);
                String[] conData=line.split(",");
                conversations.add(new Conversation(context, conData[0], conData[1], conData[2]));
            }
        });

        requests=new ConRequests(context);
        this.context=context;
    }

    public void saveData(Context ctx){
        StringBuilder temCons=new StringBuilder();
        this.requests.saveData(ctx);

        this.conversations.forEach(new Consumer<Conversation>() {
            @Override
            public void accept(Conversation conversation) {
                conversation.saveData(ctx);
                temCons.append(conversation.toSaveString());
                temCons.append("\n");
            }
        });



        FileHandler.write(ctx, this.conFile, temCons.toString(), false);
    }
}
