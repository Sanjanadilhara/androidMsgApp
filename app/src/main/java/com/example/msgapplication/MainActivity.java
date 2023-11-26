package com.example.msgapplication;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private RecyclerView conversationRecView;
    private ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        conversationRecView=findViewById(R.id.conversations);



        SharedPreferences pref=this.getSharedPreferences("cookieStore", MODE_PRIVATE);
//        pref.edit().putString("cookies", "").apply();
        String cookies=pref.getString("cookies", "");
        System.out.println("cooookies:  "+cookies);
        if (cookies.isEmpty()) {
            System.out.println(cookies);
            Intent login=new Intent(this, Login.class);
            startActivity(login);
        }

//
//        FileHandler.write(this, "conversations", "new user,65537225fbd8abae236abee2,chat505\n", true);
//        FileHandler.write(this, "conversations", "Sanjana Dilhara,2,chat2\n", true);
//        FileHandler.write(this, "conversations", "sandeepa,3,chata3\n", true);
//        FileHandler.write(this, "conversations", "name name,4,chat4\n", true);
//        FileHandler.write(this, "conversations", "another name,5,chat5\n", true);
//        FileHandler.write(this, "conversations", "Another One,6,chat6\n", true);



        chatAdapter=new ChatAdapter(this, Conversations.getInstance(this));
        conversationRecView.setLayoutManager(new LinearLayoutManager(this));
        conversationRecView.setAdapter(chatAdapter);

        startService(new Intent(this, WsService.class));
//
//        FileHandler.write(this, "flname", "{'msg':'dfddfdfdf', 'sent':true}\n", true);
//        FileHandler.write(this, "flname", "{'msg':'dfddfgfgfgfgdfdf', 'sent':false}\n", true);
//        FileHandler.write(this, "flname", "{'msg':'dfddfdfdgfgfgfgfrf', 'sent':true}\n", true);
//FileHandler.write(this, "flname", "{'msg':'dfddfdfdf', 'sent':true}\n", true);
//        FileHandler.write(this, "flname", "{'msg':'dfddfgfgfgfgdfdf', 'sent':false}\n", true);
//        FileHandler.write(this, "flname", "{'msg':'dfddfdfdgfgfgfgfrf', 'sent':true}\n", true);
//FileHandler.write(this, "flname", "{'msg':'dfddfdfdf', 'sent':true}\n", true);
//        FileHandler.write(this, "flname", "{'msg':'dfddfgfgfgfgdfdf', 'sent':false}\n", true);
//        FileHandler.write(this, "flname", "{'msg':' gardening app illustrating Android development best practices with migrating a View-based app to Jetpack Compose. To learn about', 'sent':true}\n", true);


        Conversation cc=new Conversation(this,"last added", "111", "flname");

        Conversations.getInstance(this).conversations.add(cc);




    }
}