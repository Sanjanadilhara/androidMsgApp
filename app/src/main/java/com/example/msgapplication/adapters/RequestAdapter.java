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

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {
    private Activity activity;
    private ConRequests dataList;
    public static class RequestViewHolder extends RecyclerView.ViewHolder {
        private final TextView reqName;
        private final Button accept;
        private final Avatar reqProfile;
        private final Button delete;
        private String fromId;
        private String userName;
        private int dataListIndex;


        public RequestViewHolder(Activity mainActivity, View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            reqName=(TextView) view.findViewById(R.id.reqItemName);
            reqProfile=(Avatar) view.findViewById(R.id.reqAvatar);
            accept=(Button) view.findViewById(R.id.reqAccept);
            delete=(Button) view.findViewById(R.id.reqDel);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GlobData.getInstance(mainActivity).requests.requests.remove(dataListIndex);
                    GlobData.getInstance(mainActivity).updateViews();
                }
            });
            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WsMessageHandler.sendAcceptRequest(mainActivity, fromId, userName, dataListIndex);
                }
            });


        }


        public TextView getReqName() {
            return reqName;
        }

        public Button getAccept() {
            return accept;
        }

        public Avatar getReqProfile() {
            return reqProfile;
        }

        public Button getDelete() {
            return delete;
        }

        public void setDataListIndex(int dataListIndex) {
            this.dataListIndex = dataListIndex;
        }

        public void setFromId(String fromId) {
            this.fromId = fromId;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }

    public RequestAdapter(Activity act, ConRequests data) {
        activity=act;
        dataList=data;
    }

    @Override
    public RequestAdapter.RequestViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.request_item, viewGroup, false);

        return new RequestAdapter.RequestViewHolder(activity ,view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        holder.getReqName().setText(dataList.requests.get(position).getName());
        holder.setDataListIndex(position);
        holder.setFromId(dataList.requests.get(position).getUserId());
        holder.setUserName(dataList.requests.get(position).getName());
        holder.getReqProfile().setImageDrawable(new ImageOfStr(dataList.requests.get(position).getName()));
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dataList.requests.size();
    }

}

