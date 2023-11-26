package com.example.msgapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

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
        Conversation con =Conversations.getInstance(this).conversations.get(index);
        System.out.println("after con got  len:"+con.lastMessages.size());

        messageAdapter=new MessageAdapter(this, con);

        recView.setAdapter(messageAdapter);
        recView.scrollToPosition(con.lastMessages.size()-1);

        Conversations.getInstance(this).setUpdateListener(new Conversations.EventListener() {
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
                        WsService.getSocket().send("{\"type\":\"message\",\"msgId\":\"0055855\", \"to\":\""+Conversations.getInstance(getApplicationContext()).conversations.get(index).getConversationId()+"\", \"message\":\""+messageToSend.getText()+"\", \"cookie\":\""+cookies+"\"}");
                        System.out.println(Conversations.getInstance(getApplicationContext()).conversations.get(index).getConversationId());
                        Conversations.getInstance(getApplicationContext()).conversations.get(index).lastMessages.add(new Conversation.Message(messageToSend.getText().toString(), true));
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