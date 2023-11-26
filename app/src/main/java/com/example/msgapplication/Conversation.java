package com.example.msgapplication;




import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONObject;

import java.util.ArrayList;

public class Conversation {

    public static class Message{
        public String Message;
        public boolean isSent;
        Message(String message, boolean issent){
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
    Conversation(Context ctx, String username, String conId, String filename){
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

    String getName(){
        return name;
    }
    String getLastMessage(){return "last Message "+fileName;}
    String getConversationId(){return conversationId;}

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
