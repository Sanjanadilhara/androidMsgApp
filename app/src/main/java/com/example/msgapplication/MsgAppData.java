package com.example.msgapplication;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MsgAppData extends ViewModel {

    private static MutableLiveData<Conversations> conversations=new MutableLiveData<>();
    public MutableLiveData<Conversations> getConversations(){
        return  conversations;
    }
    public void updateConverasations(){
        conversations.setValue(conversations.getValue());
    }
    public void initConversations(Conversations con){
        conversations.setValue(con);
    }

    public static Conversations getConversationsInstant(){
        return conversations.getValue();
    }
}
