package com.example.msgapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.msgapplication.cusViews.SentMessageItem;
import com.example.msgapplication.models.Conversation;
import com.example.msgapplication.R;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private Conversation conversation;
    private static int SENT_MSG=1;
    private static int RECIVED_MSG=2;
    Context ctx;
    public static class SentMessageViewHolder extends RecyclerView.ViewHolder {
        private final TextView message;
        private final ImageView msgStat;


        public SentMessageViewHolder(View view) {
            super(view);
            message = (TextView) view.findViewById(R.id.sentMsgView);
            msgStat=(ImageView) view.findViewById(R.id.sentMsgStat);

        }

        public TextView getTextView() {
            return message;
        }

        public ImageView getMsgStat() {
            return msgStat;
        }
    }
    public static class RecievedMessageViewHolder extends RecyclerView.ViewHolder {
        private final TextView message;

        public RecievedMessageViewHolder(View view) {
            super(view);
            message = (TextView) view.findViewById(R.id.recMsgView);
        }

        public TextView getTextView() {
            return message;
        }

    }


    public MessageAdapter(Context context, Conversation con) {
        conversation=con;
        ctx=context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if(viewType==SENT_MSG){

            View view=LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.sent_msg_element, viewGroup, false);

            return new SentMessageViewHolder(view);
        }
        else{
            View view=LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.recived_msg_element, viewGroup, false);

            return new RecievedMessageViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(conversation.lastMessages.get(position).isSent){
            ((SentMessageViewHolder)holder).getTextView().setText(conversation.lastMessages.get(position).Message);
            if(conversation.lastMessages.get(position).status==Conversation.MSG_SENT){
                ((SentMessageViewHolder)holder).getMsgStat().setImageResource(R.drawable.tick);
            }                   
            else if(conversation.lastMessages.get(position).status==Conversation.MSG_READ){
                ((SentMessageViewHolder)holder).getMsgStat().setImageResource(R.drawable.double_tick);
            }
            else{
                ((SentMessageViewHolder)holder).getMsgStat().setImageResource(R.drawable.waiting_clock);
            }
        }else{
            ((RecievedMessageViewHolder)holder).getTextView().setText(conversation.lastMessages.get(position).Message);
        }

    }



    @Override
    public int getItemViewType(int position) {
        return conversation.lastMessages.get(position).isSent?SENT_MSG:RECIVED_MSG;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return conversation.lastMessages.size();
    }
}