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
import com.example.msgapplication.adapters.RequestAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RequestsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RequestsFragment extends Fragment {


    private RecyclerView reqRecyclerView;
    private RequestAdapter requestAdapter;

    public RequestsFragment() {
        // Required empty public constructor
    }

    public static RequestsFragment newInstance(String param1, String param2) {
        RequestsFragment fragment = new RequestsFragment();
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
        reqRecyclerView=view.findViewById(R.id.requestRecView);
        requestAdapter=new RequestAdapter(getActivity(), GlobData.getInstance(getContext()).requests);

        reqRecyclerView.setAdapter(requestAdapter);
        reqRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        GlobData.getInstance(getContext()).setUpdateListener(new GlobData.EventListener() {
            @Override
            public void onUpdate() {
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        requestAdapter.notifyDataSetChanged();
                    }
                });
            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_requests, container, false);
    }
}