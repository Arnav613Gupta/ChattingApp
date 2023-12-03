package com.example.chattingapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.chattingapp.Adapter.Chat_user_recyclerAdapter;
import com.example.chattingapp.Model.UserProfile;
import com.example.chattingapp.R;
import com.example.chattingapp.databinding.FragmentChatBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class Chat_Fragment extends Fragment {

    FragmentChatBinding binding;
    FirebaseDatabase firebaseDatabase;
    ArrayList<UserProfile> userProfilesArrayList;



    public Chat_Fragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentChatBinding.inflate(inflater,container,false);
        firebaseDatabase=FirebaseDatabase.getInstance();
        userProfilesArrayList=new ArrayList<>();
        Chat_user_recyclerAdapter chatUserRecyclerAdapter=new Chat_user_recyclerAdapter(getContext(),userProfilesArrayList);
        binding.userRecycleView.setAdapter(chatUserRecyclerAdapter);

        firebaseDatabase.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userProfilesArrayList.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    UserProfile userProfile=dataSnapshot.getValue(UserProfile.class);
                   //skipping the current useer to appear in list
                   if (userProfile.getUID().equals(FirebaseAuth.getInstance().getUid())) {
                       continue;
                   }else {
                     userProfilesArrayList.add(userProfile);
                   }


                    chatUserRecyclerAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        return binding.getRoot();
    }
}