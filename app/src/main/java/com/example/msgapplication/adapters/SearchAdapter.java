package com.example.msgapplication.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.msgapplication.GlobData;
import com.example.msgapplication.R;
import com.example.msgapplication.WsMessageHandler;
import com.example.msgapplication.cusViews.Avatar;
import com.example.msgapplication.cusViews.ImageOfStr;
import com.example.msgapplication.models.ConRequests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.BiConsumer;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    private Activity activity;
    private ArrayList<ConRequests.Request> dataList=null;
    public ArrayList<ConRequests.Request> getDataList(){
        return dataList;
    }

    public static class SearchViewHolder extends RecyclerView.ViewHolder {
        private final TextView Name;
        private final Button sendReq;
        private final Avatar searchProfile;
        private int dataListIndex;
        private String userName;
        private String id;
        private boolean sentSuccess;


        public SearchViewHolder(Activity mainActivity, View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            Name=(TextView) view.findViewById(R.id.searchName);
            searchProfile=(Avatar) view.findViewById(R.id.searchAvatar);
            sendReq=(Button) view.findViewById(R.id.searchSendReq);


            sendReq.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WsMessageHandler.sendRequest(mainActivity, userName, id);
                }
            });




        }


        public void setDataListIndex(int dataListIndex) {
            this.dataListIndex = dataListIndex;
        }

        public TextView getName() {
            return Name;
        }

        public Button getSendReq() {
            return sendReq;
        }

        public Avatar getSearchProfile() {
            return searchProfile;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setSentRequest(boolean sentRequest) {
            this.sentSuccess = sentRequest;
        }
    }

    public SearchAdapter(Activity act) {
        this.activity=act;
        dataList=new ArrayList<>();
    }

    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.send_request_item, viewGroup, false);

        return new SearchAdapter.SearchViewHolder(activity ,view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.SearchViewHolder holder, int position) {
        holder.getName().setText(dataList.get(position).getName());
        holder.setDataListIndex(position);
        holder.setId(dataList.get(position).getUserId());
        holder.setUserName(dataList.get(position).getName());
        holder.getSearchProfile().setImageDrawable(new ImageOfStr(dataList.get(position).getName()));

        HashMap<String, ConRequests.Request> temGlobSentRequests=GlobData.getInstance(activity).requests.sentRequests;

        holder.setSentRequest(false);
        if(temGlobSentRequests.containsKey(dataList.get(position).getUserId())){
            System.out.println("sadapter: "+dataList.get(position).getUserId());
            if(temGlobSentRequests.get(dataList.get(position).getUserId()).getStatus()==ConRequests.REQUEST_SENT_SUCCESS){
                holder.getSendReq().setText("sent");
                holder.getSendReq().setBackgroundColor(-6710887);
//                holder.getSendReq().setClickable(false);
            }
        }
        else {
            holder.getSendReq().setText("Send Request");
            holder.getSendReq().setBackgroundColor(-16750849);
            holder.getSendReq().setClickable(true);
        }
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dataList==null?0:dataList.size();
    }
}

