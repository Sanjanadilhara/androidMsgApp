package com.example.msgapplication.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Users {
    static public class User{
        private String name;
        private String id;
        User(String name, String id){
            this.name=name;
            this.id=id;
        }

        public String getName() {
            return name;
        }

        public String getId() {
            return id;
        }
    }
    public ArrayList<User> users;
    Users(){
        this.users=new ArrayList<>();
    }
    public boolean addUsersByJSON(JSONArray userList){
        try{
            for(int i=0; i<userList.length(); i++){
                JSONObject temUser=userList.getJSONObject(i);
                this.users.add(new User(temUser.getString("name"), temUser.getString("_id")));
            }
        }catch (Exception e){
            return false;
        }
        return true;
    }

}
