package com.example.msgapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.msgapplication.fragments.ChatsFragment;
import com.example.msgapplication.fragments.RequestsFragment;
import com.example.msgapplication.helpers.FileHandler;
import com.example.msgapplication.models.Conversation;
import com.example.msgapplication.GlobData;
import com.example.msgapplication.R;
import com.example.msgapplication.WsService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConnectivityManager connMgr= (ConnectivityManager) getSystemService(ConnectivityManager.class);
        connMgr.registerDefaultNetworkCallback(GlobData.getInstance(this));

        TextView offlineNotify=findViewById(R.id.offlineNotify);
        GlobData.getInstance(this).setNetStatUpdateListener(new GlobData.NetStaEventListener() {
            @Override
            public void onUpdate(boolean networkAvailable) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        offlineNotify.setVisibility(networkAvailable?View.GONE:View.VISIBLE);
                    }
                });
            }
        });

        TextView navChats=findViewById(R.id.navChat);
        TextView navRequests=findViewById(R.id.navReq);
        FloatingActionButton searchActionButton=(FloatingActionButton) findViewById(R.id.searchActionButton);

        if(savedInstanceState==null){
            navChats.setBackgroundColor(1440603647);
            navRequests.setBackgroundColor(16777215);
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.mainFragContainer, ChatsFragment.class, new Bundle())
                    .commit();
        }



        GlobData.getInstance(this);

        navChats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navChats.setBackgroundColor(1440603647);
                navRequests.setBackgroundColor(16777215);
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.to_right, R.anim.to_right_out)
                        .setReorderingAllowed(true)
                        .replace(R.id.mainFragContainer, ChatsFragment.class, new Bundle())
                        .commit();

            }
        });
        navRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navChats.setBackgroundColor(16777215);
                navRequests.setBackgroundColor(1440603647);
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.to_left, R.anim.to_left_out)
                        .setReorderingAllowed(true)
                        .replace(R.id.mainFragContainer, RequestsFragment.class, new Bundle())
                        .commit();
            }
        });

        searchActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent search=new Intent(getApplicationContext(), Search.class);
                startActivity(search);
            }
        });


        SharedPreferences pref=this.getSharedPreferences("cookieStore", MODE_PRIVATE);
//        pref.edit().putString("cookies", "").apply();
        String cookies=pref.getString("cookies", "");
        System.out.println("cooookies:  "+cookies);
        if (cookies.isEmpty()) {
            System.out.println(cookies);
            Intent login=new Intent(this, Login.class);
            startActivity(login);
        }
        else{
            startService(new Intent(this, WsService.class));
        }

//
//        FileHandler.write(this, "conversations", "new user,65537225fbd8abae236abee2,chat505\n", true);
//        FileHandler.write(this, "conversations", "Sanjana Dilhara,2,chat2\n", true);
//        FileHandler.write(this, "conversations", "sandeepa,3,chata3\n", true);
//        FileHandler.write(this, "conversations", "name name,4,chat4\n", true);
//        FileHandler.write(this, "conversations", "another name,5,chat5\n", true);
//        FileHandler.write(this, "conversations", "Another One,6,chat6\n", true);






//
//        FileHandler.write(this, "flname", "{'msg':'dfddfgfgfgfgdfdf', 'sent':false}\n", true);
//        FileHandler.write(this, "flname", "{'msg':'dfddfdfdgfgfgfgfrf', 'sent':true}\n", true);
//FileHandler.write(this, "flname", "{'msg':'dfddfdfdf', 'sent':true}\n", true);
//        FileHandler.write(this, "flname", "{'msg':'dfddfgfgfgfgdfdf', 'sent':false}\n", true);
//        FileHandler.write(this, "flname", "{'msg':'dfddfdfdgfgfgfgfrf', 'sent':true}\n", true);
//FileHandler.write(this, "flname", "{'msg':'dfddfdfdf', 'sent':true}\n", true);
//        FileHandler.write(this, "flname", "{'msg':'dfddfgfgfgfgdfdf', 'sent':false}\n", true);
//        FileHandler.write(this, "flname", "{'msg':' gardening app illustrating Android development best practices with migrating a View-based app to Jetpack Compose. To learn about', 'sent':true}\n", true);


//        Conversation cc=new Conversation(this,"sanjana dilhra", "657b5883adeaca40225cc21b", "flname");

//        GlobData.getInstance(this).conversations.add(cc);




    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GlobData.getInstance(this).saveData(this);
    }

}