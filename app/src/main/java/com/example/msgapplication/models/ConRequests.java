package com.example.msgapplication.models;

import android.content.Context;

import androidx.annotation.Nullable;

import com.example.msgapplication.helpers.FileHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ConRequests {


    public static int REQUEST_WAITING_FOR_RESPONSE=2;
    public static int RECIVED_REQUEST=0;
    public static int REQUEST_ITEM=1;
    public static int REQUEST_SENT_SUCCESS=3;
    public static int ACCEPTED_REQUEST=4;
    public static class Request{
        private Integer status;
        private String id;
        private boolean isSent;
        private String name;
        public Request(String userName, String id, int status){
            this.name=userName;
            this.id=id;
            this.isSent=status==RECIVED_REQUEST?false:true;
            this.status=status;
        }

        public String getUserId() {
            return this.id;
        }

        public String getName() {
            return name;
        }

        public boolean isSent() {
            return isSent;
        }
        public String toJSONString(){
            return "{\"name\":\""+this.name+"\", \"id\":\""+this.id+"\", \"isSent\":"+(this.isSent?"true":"false")+", \"status\":"+this.status.toString()+"}";
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public Request(String id){
            this.id=id;
        }


        @Override
        public boolean equals(@Nullable Object obj) {
            if(obj==null){
                return false;
            }
            return ((Request)obj).getUserId().equals(this.id);
        }
    }

    private final String reqFile="requests";
    public ArrayList<Request> requests;
    public HashMap<String, Request> sentRequests;
    public ConRequests(Context ctx){
        requests=new ArrayList<>();
        sentRequests=new HashMap<>();
        FileHandler.readFile(ctx, reqFile, new FileHandler.Content() {
            @Override
            public void read(String line) {
                try{
                    JSONObject temReqData=new JSONObject(line);
                    if(temReqData.getBoolean("isSent")){
                        sentRequests.put(temReqData.getString("id"), new Request(temReqData.getString("name"), temReqData.getString("id"), temReqData.getInt("status")));
                    }
                    else{
                        requests.add(new Request(temReqData.getString("name"), temReqData.getString("id"), temReqData.getInt("status")));
                    }


                    System.out.println("this is a line:"+line);

                }catch (Exception e){}
            }
        });
    }
    public void saveData(Context ctx){
        StringBuilder toWrite=new StringBuilder();

        sentRequests.forEach(new BiConsumer<String, Request>() {
            @Override
            public void accept(String s, Request request) {
                toWrite.append(request.toJSONString());
                toWrite.append("\n");
            }
        });
        requests.forEach(new Consumer<Request>() {
            @Override
            public void accept(Request request) {
                toWrite.append(request.toJSONString());
                toWrite.append("\n");
            }
        });
        FileHandler.write(ctx, this.reqFile, toWrite.toString(), false);
    }
    public int getSentRequestById(String id){
        for(int i=0; i<sentRequests.size(); i++){
            if(sentRequests.get(i).id==id){
                return i;
            }
        }
        return -1;
    }
    public int getRequestById(String id){
        for(int i=0; i<sentRequests.size(); i++){
            if(requests.get(i).id==id){
                return i;
            }
        }
        return -1;
    }
}
