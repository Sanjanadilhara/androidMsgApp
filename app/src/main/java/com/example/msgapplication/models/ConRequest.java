package com.example.msgapplication.models;

import android.content.Context;

import com.example.msgapplication.helpers.FileHandler;

import java.util.ArrayList;

public class ConRequest {

    public class Request{
        private String from;
        private String name;
        Request(String userName, String id){
            name=userName;
            from=id;
        }

        public String getFrom() {
            return from;
        }

        public String getName() {
            return name;
        }
    }

    private final String reqFile="requests";
    public ArrayList<Request> requests;
    public ConRequest(Context ctx){
        requests=new ArrayList<>();
        FileHandler.readFile(ctx, reqFile, new FileHandler.Content() {
            @Override
            public void read(String line) {
                System.out.println("this is a line:"+line);
                String[] conData=line.split(",");
                requests.add(new Request( conData[0], conData[1]));
            }
        });
    }
}
