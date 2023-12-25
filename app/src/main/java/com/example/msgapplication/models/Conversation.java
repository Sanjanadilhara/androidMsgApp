package com.example.msgapplication.models;




import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.msgapplication.helpers.FileHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Consumer;

public class Conversation {

    public static int MSG_SENDING=0;
    public static int MSG_SENT=1;
    public static int MSG_READ=2;

    public static class Message{
        public String Message;
        public Integer status=MSG_SENDING;
        public Boolean isSent;
        public Message(String message, boolean issent){
            Message =message;
            isSent=issent;
        }
        public Message(String message, boolean issent, int status){
            Message =message;
            isSent=issent;
            this.status=status;
        }

        public String toJSONString(){
            return "{\"msg\":"+this.Message+", \"sent\":"+this.isSent.toString()+", \"status\":"+this.status.toString()+"}";
        }

    }
    private String conversationId=null;
    private String name=null;
    private String profile=null;
    private String fileName=null;
    public ArrayList<Message> lastMessages=null;
    private Integer numOfNonStoredMessages=null;
    Context context=null;
    public Conversation(String conId) {
        this.conversationId = conId;
    }



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
                    lastMessages.add(new Message(msgData.getString("msg"), msgData.getBoolean("sent"), msgData.getInt("status")));
                }
                catch (Exception e){

                }
            }
        });
    }

    public String getName(){
        return name;
    }
    public String getLastMessage(){
        return this.lastMessages.size()<=0?"There is no message yet":(this.lastMessages.get(this.lastMessages.size()-1).Message);
    }
    public String getConversationId(){return conversationId;}

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj==null){
            return false;
        }
        return this.conversationId.equals(((Conversation)obj).getConversationId());
    }

    public void saveData(Context ctx){
        StringBuilder temMessages=new StringBuilder();
        this.lastMessages.forEach(new Consumer<Message>() {
            @Override
            public void accept(Message message) {
                temMessages.append(message.toJSONString());
                temMessages.append("\n");
            }
        });
        FileHandler.write(ctx, this.fileName, temMessages.toString(), false);
    }
    public String toSaveString(){
        return this.name+","+this.conversationId+","+this.fileName;
    }

}
