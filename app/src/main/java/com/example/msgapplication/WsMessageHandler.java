package com.example.msgapplication;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.msgapplication.models.ConRequests;
import com.example.msgapplication.models.Conversation;

import org.json.JSONException;
import org.json.JSONObject;

public class WsMessageHandler {
    private static String cookies;
    public static void initializeConnection(Context ctx){
        SharedPreferences pref=ctx.getSharedPreferences("cookieStore", MODE_PRIVATE);
        cookies=pref.getString("cookies", "");
        WsService.getSocket().send("{\"type\":\"init\", \"cookie\":\""+cookies+"\"}");
    }
    public static void handleIncommingMessages(JSONObject data, Context ctx){
        System.out.println(Thread.currentThread().getName());
        try {
            switch (data.getString("type")){
                case "message":
                    GlobData.getInstance(ctx).conversations.get(GlobData.getInstance(ctx).conversations.indexOf(new Conversation(data.getString("from")))).lastMessages.add(new Conversation.Message(data.getString("message"), false));
                    GlobData.getInstance(ctx).updateViews();
                    break;
                case "messageStat":
                    break;
                case "init":
                    WsService.isAuthorized=data.getInt("stat")==1?true:false;
                    break;
                case "request":
                    if(!GlobData.getInstance(ctx).requests.requests.contains(new ConRequests.Request(data.getString("from")))){
                        GlobData.getInstance(ctx).requests.requests.add(new ConRequests.Request(data.getString("username"), data.getString("from"), ConRequests.RECIVED_REQUEST));
                        GlobData.getInstance(ctx).updateViews();
                    }
                    break;
                case "requestResponse":
                    if(GlobData.getInstance(ctx).requests.sentRequests.containsKey(data.getString("to"))){
                        GlobData.getInstance(ctx).requests.sentRequests.get(data.getString("to")).setStatus(ConRequests.REQUEST_SENT_SUCCESS);
                        System.out.println("wsmesagecontroll requestRespose handled");

                    }
                    GlobData.getInstance(ctx).updateViews();
//                    Toast.makeText(ctx, "respose: "+data.getString("to"), Toast.LENGTH_SHORT);
                    break;
                case "requestAcceptRespose":
                    break;
                case "requestAccept":
                    if(GlobData.getInstance(ctx).requests.sentRequests.containsKey(data.getString("to"))){
                        GlobData.getInstance(ctx).requests.sentRequests.get(data.getString("to")).setStatus(ConRequests.ACCEPTED_REQUEST);
                    }
                    if(!GlobData.getInstance(ctx).conversations.contains(new Conversation(data.getString("to")))){

                        GlobData.getInstance(ctx).conversations.addFirst(new Conversation(ctx, data.getString("username"), data.getString("to"), "con"+data.getString("to")));
                        GlobData.getInstance(ctx).updateViews();
                    }
                    break;

            }
        }catch (Exception e){

        }

    }
    public static boolean sendRequest(Context ctx, String name, String toId){
        try{
        if(WsService.getSocket()!=null && WsService.isAuthorized){
            GlobData.getInstance(ctx).requests.sentRequests.put(toId,new ConRequests.Request(name, toId, ConRequests.REQUEST_WAITING_FOR_RESPONSE));
            JSONObject temReq=new JSONObject();
            temReq.put("type", "request");
            temReq.put("to", toId);
            temReq.put("reqId", "android");
            WsService.getSocket().send(temReq.toString());
        }
        else{
            Toast.makeText(ctx, "you are offline !", Toast.LENGTH_SHORT).show();
            WsService.startWS();
        }

        }catch (Exception e){

        }
        return true;
    }
    public static boolean sendAcceptRequest(Context ctx, String from, String userName, int reqIndex){
        try{
            if(WsService.getSocket()!=null && WsService.isAuthorized){
                JSONObject temReq=new JSONObject();
                temReq.put("type", "request");
                temReq.put("accept",1);
                temReq.put("from", from);
                WsService.getSocket().send(temReq.toString());
                if(!GlobData.getInstance(ctx).conversations.contains(new Conversation(from))){
                    GlobData.getInstance(ctx).conversations.addFirst(new Conversation(ctx, userName, from, "con"+from));
                    GlobData.getInstance(ctx).saveData(ctx);
                }
                GlobData.getInstance(ctx).requests.requests.remove(reqIndex);
                GlobData.getInstance(ctx).updateViews();

            }
            else{
                Toast.makeText(ctx, "you are offline !", Toast.LENGTH_SHORT).show();
                WsService.startWS();
            }

        }catch (Exception e){

        }
        return true;
    }

}
