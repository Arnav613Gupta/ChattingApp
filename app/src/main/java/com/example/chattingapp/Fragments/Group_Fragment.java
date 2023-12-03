package com.example.chattingapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.chattingapp.Adapter.Group_user_Adapter;
import com.example.chattingapp.Group_Name_Activity;
import com.example.chattingapp.Model.GroupModel;
import com.example.chattingapp.NameActivity;
import com.example.chattingapp.R;
import com.example.chattingapp.databinding.FragmentGroupBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;

public class Group_Fragment extends Fragment {

   FragmentGroupBinding binding;
   ArrayList<GroupModel>groupArrayList;
   HashSet<String>groupToShow;

   FirebaseDatabase database;
   Group_user_Adapter groupAdapter;



    public Group_Fragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        groupArrayList=new ArrayList<>();
        groupToShow=new HashSet<>();
        database=FirebaseDatabase.getInstance();

        binding=FragmentGroupBinding.inflate(getLayoutInflater(),container,false);
        binding.addgroupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Group name screen
                Intent groupIntent=new Intent(getContext(), Group_Name_Activity.class);
                startActivity(groupIntent);





            }
        });




         groupAdapter=new Group_user_Adapter(getContext(),groupArrayList);
        binding.groupChatRecycleView.setAdapter(groupAdapter);
        database.getReference("Users").child(FirebaseAuth.getInstance().getUid()).child("Joined_Groups").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                groupToShow.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    groupToShow.add(dataSnapshot.getValue(String.class));
                    showGroupsMeth();

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        showGroupsMeth();














        return binding.getRoot();
    }
    private void showGroupsMeth(){
        database.getReference("Groups").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                groupArrayList.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    GroupModel groupUserData= dataSnapshot.getValue(GroupModel.class);
                    if(groupToShow.contains(groupUserData.getGroupUID())){
                        groupArrayList.add(groupUserData);
                        groupAdapter.notifyDataSetChanged();
                    }



                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}