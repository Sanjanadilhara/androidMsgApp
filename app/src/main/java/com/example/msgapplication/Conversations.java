package com.example.msgapplication;

import android.content.Context;

import java.util.ArrayList;

public class Conversations {

    public ArrayList<EventListener> events=new ArrayList<>();
    static interface EventListener{
        public void onUpdate();
    }
    public void setUpdateListener(EventListener e){
        events.add(e);
    }
    private static Conversations staticCons;
    public ArrayList<Conversation> conversations;
    private  String conFile="conversations";

    public static synchronized Conversations getInstance(Context ctx){
        if(staticCons == null){
            staticCons=new Conversations(ctx);
            System.out.println("null here");
        }
        return staticCons;
    }
    Conversations(Context context){
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
    }
}
