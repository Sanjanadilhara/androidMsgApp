package com.example.msgapplication.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.msgapplication.activities.ConversationView;
import com.example.msgapplication.GlobData;
import com.example.msgapplication.R;
import com.example.msgapplication.cusViews.Avatar;


public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private Activity activity;
    private GlobData dataList;
    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final TextView lastMsg;
        private final Avatar profile;
        private final LinearLayout card;
        private int dataListIndex;


        public ChatViewHolder(Activity mainActivity, View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            name=(TextView) view.findViewById(R.id.conName);
            lastMsg=(TextView) view.findViewById(R.id.lastMsg);
            profile=(Avatar) view.findViewById(R.id.consAvatar);
            card=(LinearLayout) view.findViewById(R.id.consCard);

            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sendIntent = new Intent(mainActivity, ConversationView.class);
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.setType("text/plain");
                    sendIntent.putExtra("index", dataListIndex);
                    mainActivity.startActivity(sendIntent);
                }
            });

        }

        public TextView getName(){return name;}
        public TextView getLastMsg(){return lastMsg;}
        public Avatar getProfile(){return profile;}

    }

    public ChatAdapter(Activity act, GlobData data) {
        activity=act;
        dataList=data;
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.chat_card, viewGroup, false);

        return new ChatViewHolder(activity ,view);
    }

    @Override
    public void onBindViewHolder(ChatViewHolder viewHolder, int position) {
        viewHolder.getName().setText(dataList.conversations.get(position).getName());
        viewHolder.getLastMsg().setText(dataList.conversations.get(position).getLastMessage());
        viewHolder.dataListIndex=position;

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dataList.conversations.size();
    }
}
