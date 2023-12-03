package com.example.chattingapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chattingapp.Model.UserProfile;
import com.example.chattingapp.R;
import com.example.chattingapp.SelectPeopleForGroup;
import com.example.chattingapp.databinding.RowsChatUserLayoutBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class groupSelectParticipantsAdapter extends RecyclerView.Adapter<groupSelectParticipantsAdapter.ViewHolder> {
    Context context;
    ArrayList<UserProfile> UserShowList;
    ArrayList<String> SelectedParticipants=new ArrayList<>();
    public groupSelectParticipantsAdapter(Context context, ArrayList<UserProfile> UserShowList){
        this.context=context;
        this.UserShowList=UserShowList;}
    @NonNull
    @Override
    public groupSelectParticipantsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.rows_chat_user_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull groupSelectParticipantsAdapter.ViewHolder holder, int position) {
        UserProfile userProfile=UserShowList.get(position);

        holder.binding.ProfileNameChatLluout.setText(userProfile.getNAME());
        Picasso.get().load(userProfile.getPROFILEIMAGE()).placeholder(R.drawable.blank_profile_pic).into(holder.binding.ProfilePicChatLluout);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                //adding and removing participants from list
                if(!SelectedParticipants.contains(userProfile.getUID())){
                    SelectedParticipants.add(userProfile.getUID());
                    holder.binding.userdesignCardView.setCardBackgroundColor(context.getColor(R.color.blue));

                }else {
                    SelectedParticipants.remove(userProfile.getUID());
                    holder.binding.userdesignCardView.setCardBackgroundColor(context.getColor(R.color.white));
                }


            }
        });


    }

    @Override
    public int getItemCount() {
        return UserShowList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RowsChatUserLayoutBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding=RowsChatUserLayoutBinding.bind(itemView);
        }
    }
    public ArrayList<String> getSelectedParticipants(){
        return SelectedParticipants;
    }
}
