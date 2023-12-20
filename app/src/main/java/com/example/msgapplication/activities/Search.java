package com.example.msgapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.msgapplication.GlobData;
import com.example.msgapplication.R;
import com.example.msgapplication.adapters.SearchAdapter;
import com.example.msgapplication.helpers.Connection;
import com.example.msgapplication.models.ConRequests;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Search extends AppCompatActivity {
    private ImageButton searchButton;
    private EditText searchTextBox;
    private RecyclerView searchResults;
    private SearchAdapter searchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
         searchButton=findViewById(R.id.searchuserButton);
         searchTextBox=findViewById(R.id.searchTextBox);
         searchResults=findViewById(R.id.searchRecView);

         searchAdapter=new SearchAdapter(this);
         searchResults.setLayoutManager(new LinearLayoutManager(this));
         searchResults.setAdapter(searchAdapter);

        Activity act=this;

        GlobData.getInstance(this).setUpdateListener(new GlobData.EventListener() {
            @Override
            public void onUpdate() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("search serach adapter dacta changed");
                        searchAdapter.notifyDataSetChanged();
                    }
                });
            }
        });


         searchButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 try {
                     searchAdapter.getDataList().clear();
                     JSONObject searchData=new JSONObject();
                     searchData.put("key", searchTextBox.getText());
                     Connection.getInstance(act).post("http://" + GlobData.serverAddress + ":80/search", searchData,  new Connection.ExecuteNetResult() {
                         @Override
                         public void run(JSONObject data) {

                             try {
                                 JSONArray temDataArr=data.getJSONArray("users");
                                 for(int i=0; i<temDataArr.length();i++){
                                     searchAdapter.getDataList().add(new ConRequests.Request(temDataArr.getJSONObject(i).getString("username"), temDataArr.getJSONObject(i).getString("_id"), ConRequests.REQUEST_ITEM));

                                 }
                                searchAdapter.notifyDataSetChanged();
                             } catch (JSONException e) {
                                 System.out.println("exeption: "+e.toString());
                             }
                         }
                     });
                 } catch (JSONException e) {
                     System.out.println("ee:"+e.toString());
                 }
             }
         });

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        GlobData.getInstance(this).saveData(this);
    }

}