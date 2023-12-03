package com.example.chattingapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chattingapp.Fragments.Status_Fragment;
import com.example.chattingapp.MainActivity;
import com.example.chattingapp.Model.StatusImg;
import com.example.chattingapp.Model.UserStatusModel;
import com.example.chattingapp.R;
import com.example.chattingapp.databinding.StatusItemLayoutBinding;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import omari.hamza.storyview.StoryView;
import omari.hamza.storyview.callback.StoryClickListeners;
import omari.hamza.storyview.model.MyStory;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.ViewHolder> {
    Context context;
    ArrayList<UserStatusModel> userStatusModelArrayList;

    public StatusAdapter(Context context, ArrayList<UserStatusModel> userStatusModelArrayList) {
        this.context = context;
        this.userStatusModelArrayList = userStatusModelArrayList;
    }

    @NonNull
    @Override
    public StatusAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.status_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatusAdapter.ViewHolder holder, int position) {
        if(!userStatusModelArrayList.isEmpty()){
        try{



        UserStatusModel userStatus=userStatusModelArrayList.get(position);
        StatusImg lastImgedata=userStatus.getStatusImgArrayList().get(userStatus.getStatusImgArrayList().size()-1);
        Picasso.get().load(lastImgedata.getImgUrl()).placeholder(R.drawable.blank_profile_pic).into(holder.binding.circularImageView);
        holder.binding.circularStatusView.setPortionsCount(userStatus.getStatusImgArrayList().size());


        holder.binding.circularStatusView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<MyStory> myStories = new ArrayList<>();
                for (StatusImg statusImg:userStatus.getStatusImgArrayList()){
                    myStories.add(new MyStory(statusImg.getImgUrl()));
                }

                new StoryView.Builder(((MainActivity)context).getSupportFragmentManager())
                        .setStoriesList(myStories) // Required
                        .setStoryDuration(5000) // Default is 2000 Millis (2 Seconds)
                        .setTitleText(userStatus.getName()) // Default is Hidden
                        .setSubtitleText("") // Default is Hidden
                        .setTitleLogoUrl(userStatus.getProfileImage()) // Default is Hidden
                        .setStoryClickListeners(new StoryClickListeners() {
                            @Override
                            public void onDescriptionClickListener(int position) {
                                //your action
                            }

                            @Override
                            public void onTitleIconClickListener(int position) {
                                //your action
                            }
                        }) // Optional Listeners
                        .build() // Must be called before calling show method
                        .show();

            }
        });}catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }}

    }

    @Override
    public int getItemCount() {
        return userStatusModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        StatusItemLayoutBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding=StatusItemLayoutBinding.bind(itemView);
            
        }
    }
}
