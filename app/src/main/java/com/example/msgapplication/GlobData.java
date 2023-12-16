package com.example.msgapplication;

import android.content.Context;

import com.example.msgapplication.helpers.FileHandler;
import com.example.msgapplication.models.ConRequest;
import com.example.msgapplication.models.Conversation;

import java.util.ArrayList;
import java.util.function.Consumer;

public class GlobData {

    public ArrayList<EventListener> events=new ArrayList<>();
    public static interface EventListener{
        public void onUpdate();
    }
    public void updateViews(){
        events.forEach(new Consumer<EventListener>() {
            @Override
            public void accept(EventListener eventListener) {
                eventListener.onUpdate();
            }
        });
    }
    public void setUpdateListener(EventListener e){
        events.add(e);
    }
    private static GlobData staticCons;
    public ArrayList<Conversation> conversations;
    public ConRequest requests;
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
        conversations=new ArrayList<>();
        FileHandler.readFile(context, conFile, new FileHandler.Content() {
            @Override
            public void read(String line) {
                System.out.println("this is a line:"+line);
                String[] conData=line.split(",");
                conversations.add(new Conversation(context, conData[0], conData[1], conData[2]));
            }
        });

        requests=new ConRequest(context);
    }
}
