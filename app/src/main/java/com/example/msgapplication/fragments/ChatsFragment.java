package com.example.msgapplication.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.msgapplication.GlobData;
import com.example.msgapplication.R;
import com.example.msgapplication.WsService;
import com.example.msgapplication.adapters.ChatAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatsFragment extends Fragment {

    private RecyclerView conversationRecView;
    private ChatAdapter chatAdapter;
    public ChatsFragment() {
        // Required empty public constructor
    }


    public static ChatsFragment newInstance() {
        ChatsFragment fragment = new ChatsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        WsService.startWS();



        conversationRecView=view.findViewById(R.id.conversations);
        chatAdapter=new ChatAdapter(getActivity(), GlobData.getInstance(getContext()));
        conversationRecView.setLayoutManager(new LinearLayoutManager(getContext()));
        conversationRecView.setAdapter(chatAdapter);

        GlobData.getInstance(getContext()).setUpdateListener(new GlobData.EventListener() {
            @Override
            public void onUpdate() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        chatAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.chats_fragment_view, container, false);

    }
}