package com.example.msgapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.msgapplication.GlobData;
import com.example.msgapplication.R;
import com.example.msgapplication.WsService;
import com.example.msgapplication.adapters.MessageAdapter;
import com.example.msgapplication.models.Conversation;

public class ConversationView extends AppCompatActivity {
    public RecyclerView recView;
    public MessageAdapter messageAdapter;
    public ImageButton sendButton;
    public EditText messageToSend;
    String cookies;
    int index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_view);

        SharedPreferences pref=this.getSharedPreferences("cookieStore", MODE_PRIVATE);
        cookies=pref.getString("cookies", "");


        recView=findViewById(R.id.conversationRecView);
        messageToSend=(EditText) findViewById(R.id.message_to_send);
        sendButton=(ImageButton)findViewById(R.id.send_button);


        recView.setLayoutManager(new LinearLayoutManager(this));

        index=getIntent().getIntExtra("index", -1);
        Conversation con = GlobData.getInstance(this).conversations.get(index);
        System.out.println("after con got  len:"+con.lastMessages.size());

        messageAdapter=new MessageAdapter(this, con);

        recView.setAdapter(messageAdapter);
        recView.scrollToPosition(con.lastMessages.size()-1);

        GlobData.getInstance(this).setUpdateListener(new GlobData.EventListener() {
            @Override
            public void onUpdate() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        messageAdapter.notifyDataSetChanged();
                    }
                });

            }
        });


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WsService.getHandler().post(new Runnable() {
                    @Override
                    public void run() {

                        WsService.getSocket().send("{\"type\":\"message\",\"msgId\":\"0055855\", \"to\":\""+ GlobData.getInstance(getApplicationContext()).conversations.get(index).getConversationId()+"\", \"message\":\""+messageToSend.getText()+"\"}");
                        System.out.println(GlobData.getInstance(getApplicationContext()).conversations.get(index).getConversationId());
                        GlobData.getInstance(getApplicationContext()).conversations.get(index).lastMessages.add(new Conversation.Message(messageToSend.getText().toString(), true));
                        messageToSend.setText("");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                messageAdapter.notifyDataSetChanged();
                            }
                        });

                    }
                });
            }
        });

    }
}