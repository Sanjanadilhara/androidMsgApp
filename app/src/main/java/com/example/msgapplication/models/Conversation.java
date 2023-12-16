package com.example.msgapplication.models;




import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.msgapplication.helpers.FileHandler;

import org.json.JSONObject;

import java.util.ArrayList;

public class Conversation {

    public static class Message{
        public String Message;
        public boolean isSent;
        public Message(String message, boolean issent){
            Message =message;
            isSent=issent;
        }

    }
    private String conversationId;
    private String name;
    private String profile;
    private String fileName;
    public ArrayList<Message> lastMessages;
    private int numOfNonStoredMessages=0;
    Context context;
    public Conversation(Context ctx, String username, String conId, String filename){
        context=ctx;
        conversationId=conId;
        name=username;
        fileName=filename;
        lastMessages= new ArrayList<>();
        FileHandler.readFile(context, fileName, new FileHandler.Content() {
            @Override
            public void read(String line) {
                System.out.println("reading line");
                try{
                    JSONObject msgData=new JSONObject(line);
                    lastMessages.add(new Message(msgData.getString("msg"), msgData.getBoolean("sent")));
                }
                catch (Exception e){

                }
            }
        });
    }

    public String getName(){
        return name;
    }
    public String getLastMessage(){return "last Message "+fileName;}
    public String getConversationId(){return conversationId;}

    @Override
    public boolean equals(@Nullable Object obj) {

        return conversationId.equals(obj)?true:false;
//        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return conversationId.hashCode();
    }

    @NonNull
    @Override
    public String toString() {
        return conversationId.toString();
    }
}
