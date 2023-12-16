package com.example.msgapplication;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.msgapplication.models.Conversation;

import org.json.JSONObject;

public class WsMessageHandler {
    private static String cookies;
    public static void initializeConnection(Context ctx){
        SharedPreferences pref=ctx.getSharedPreferences("cookieStore", MODE_PRIVATE);
        cookies=pref.getString("cookies", "");
        WsService.getSocket().send("{\"type\":\"init\", \"cookie\":\""+cookies+"\"}");
    }
    public static void handleIncommingMessages(JSONObject data, Context ctx){
        try {
            switch (data.getString("type")){
                case "message":
                    int index=-1;
                    for (int y = 0; y< GlobData.getInstance(ctx).conversations.size(); y++){
                        System.out.println(GlobData.getInstance(ctx).conversations.get(y).getConversationId()+" cid  "+data.getString("from"));
                        if(GlobData.getInstance(ctx).conversations.get(y).getConversationId().equals(data.getString("from"))){
                            index=y;
                        }
                    }
                    System.out.println("message get index: "+index+"\nfrom: "+data.getString("from")+"\nconsid: "+ GlobData.getInstance(ctx).conversations.get(index).hashCode()+"hash:"+data.getString("from").hashCode());
                    GlobData.getInstance(ctx).conversations.get(index).lastMessages.add(new Conversation.Message(data.getString("message"), false));

                    GlobData.getInstance(ctx).events.forEach(eventListener -> {
                        eventListener.onUpdate();
                    });

                    break;
                case "messageStat":
                    break;
            }
        }catch (Exception e){

        }

    }
    public static boolean sendRequest(){
        return true;
    }

}
