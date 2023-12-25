package com.example.msgapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;

import com.example.msgapplication.adapters.FragmentsAdapter;
import com.example.msgapplication.fragments.ChatsFragment;
import com.example.msgapplication.fragments.RequestsFragment;
import com.example.msgapplication.helpers.FileHandler;
import com.example.msgapplication.models.Conversation;
import com.example.msgapplication.GlobData;
import com.example.msgapplication.R;
import com.example.msgapplication.WsService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        SharedPreferences pref = this.getSharedPreferences("cookieStore", MODE_PRIVATE);
//        pref.edit().putString("cookies", "").apply();
        String cookies = pref.getString("cookies", "");
        System.out.println("cooookies:  " + cookies);
        if (cookies.isEmpty()) {
            System.out.println(cookies);
            Intent login = new Intent(this, Login.class);
            startActivity(login);
        } else {
            if (!WsService.startWS()) {
                System.out.println("strtService.......");
                startService(new Intent(this, WsService.class));
            }
        }


        setContentView(R.layout.activity_main);

        Toolbar mainToolbar=findViewById(R.id.mainToolbar);
        setSupportActionBar(mainToolbar);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(ConnectivityManager.class);
        connMgr.registerDefaultNetworkCallback(GlobData.getInstance(this));

        TextView offlineNotify = findViewById(R.id.offlineNotify);
        GlobData.getInstance(this).setNetStatUpdateListener(new GlobData.NetStaEventListener() {
            @Override
            public void onUpdate(boolean networkAvailable) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        offlineNotify.setVisibility(networkAvailable ? View.GONE : View.VISIBLE);
                    }
                });
            }
        });


        FloatingActionButton searchActionButton = (FloatingActionButton) findViewById(R.id.searchActionButton);
        TabLayout tabs=findViewById(R.id.mainTabs);
        ViewPager2 viewPager=findViewById(R.id.mainViewPager);

        FragmentsAdapter pages=new FragmentsAdapter(getSupportFragmentManager(), getLifecycle());
        pages.addFragment(new ChatsFragment(), "Chats");
        pages.addFragment(new RequestsFragment(), "Requests");







        viewPager.setAdapter(pages);
        viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);


        new TabLayoutMediator(tabs, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(pages.titles.get(position));
            }
        }).attach();

        GlobData.getInstance(this);


        searchActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent search = new Intent(getApplicationContext(), Search.class);
                startActivity(search);
            }
        });


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_act_menu,menu);
        return true;
    }
}