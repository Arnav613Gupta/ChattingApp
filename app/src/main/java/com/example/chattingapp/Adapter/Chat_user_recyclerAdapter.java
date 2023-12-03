package com.example.chattingapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chattingapp.Chat_detail_Activity;
import com.example.chattingapp.Model.UserProfile;
import com.example.chattingapp.R;
import com.example.chattingapp.databinding.RowsChatUserLayoutBinding;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.util.ArrayList;

public class Chat_user_recyclerAdapter extends RecyclerView.Adapter<Chat_user_recyclerAdapter.ViewHolder> {

    Context context;
    ArrayList<UserProfile> UserarrayList;
    public Chat_user_recyclerAdapter(Context context,ArrayList<UserProfile> UserarrayList){
        this.context=context;
        this.UserarrayList=UserarrayList;

    }



    @NonNull
    @Override
    public Chat_user_recyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view=LayoutInflater.from(context).inflate(R.layout.rows_chat_user_layout,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull Chat_user_recyclerAdapter.ViewHolder holder, int position) {
        UserProfile userProfile=UserarrayList.get(position);

        holder.binding.ProfileNameChatLluout.setText(userProfile.getNAME());
        Picasso.get().load(userProfile.getPROFILEIMAGE()).placeholder(R.drawable.blank_profile_pic).into(holder.binding.ProfilePicChatLluout);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toChatDeta_activityintent=new Intent(context, Chat_detail_Activity.class);

                toChatDeta_activityintent.putExtra("receiverName",userProfile.getNAME());
                toChatDeta_activityintent.putExtra("receiverUID",userProfile.getUID());
                toChatDeta_activityintent.putExtra("receiverPic",userProfile.getPROFILEIMAGE());

                context.startActivity(toChatDeta_activityintent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return UserarrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RowsChatUserLayoutBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding=RowsChatUserLayoutBinding.bind(itemView);

        }
    }

}
