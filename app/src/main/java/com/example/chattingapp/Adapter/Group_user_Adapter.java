package com.example.chattingapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chattingapp.Fragments.Group_Fragment;
import com.example.chattingapp.Model.GroupModel;
import com.example.chattingapp.R;
import com.example.chattingapp.databinding.RowsChatUserLayoutBinding;
import com.example.chattingapp.group_chat_detail_acctivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Group_user_Adapter extends RecyclerView.Adapter<Group_user_Adapter.ViewHoolder> {
    Context context;
    ArrayList<GroupModel>groupArrayList;
    public Group_user_Adapter(Context context, ArrayList<GroupModel> groupsArrayList){
        this.context=context;
        this.groupArrayList=groupsArrayList;

    }
    @NonNull
    @Override
    public Group_user_Adapter.ViewHoolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.rows_chat_user_layout,parent,false);
        return new ViewHoolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Group_user_Adapter.ViewHoolder holder, int position) {
        GroupModel groupModel=groupArrayList.get(position);
        Picasso.get().load(groupModel.getGroupImg()).placeholder(R.drawable.blank_profile_pic).into(holder.binding.ProfilePicChatLluout);
       holder.binding.ProfileNameChatLluout.setText(groupModel.getGroupName());
       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent chat_detail_intent=new Intent(context,group_chat_detail_acctivity.class);
               chat_detail_intent.putExtra("GroupName",groupModel.getGroupName());
               chat_detail_intent.putExtra("GroupImg",groupModel.getGroupImg());
               chat_detail_intent.putExtra("GroupId",groupModel.getGroupUID());
               context.startActivity(chat_detail_intent);
           }
       });

    }

    @Override
    public int getItemCount() {
        return groupArrayList.size();
    }

    public class ViewHoolder extends RecyclerView.ViewHolder {
        RowsChatUserLayoutBinding binding;
        public ViewHoolder(@NonNull View itemView) {
            super(itemView);
            binding=RowsChatUserLayoutBinding.bind(itemView);
        }
    }
}
