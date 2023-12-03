package com.example.chattingapp;

import androidx.annotation.NonNull;
import androidx.annotation.ReturnThis;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.chattingapp.Adapter.groupSelectParticipantsAdapter;
import com.example.chattingapp.Fragments.Group_Fragment;
import com.example.chattingapp.Model.GroupModel;
import com.example.chattingapp.Model.UserProfile;
import com.example.chattingapp.databinding.ActivitySelectPeopleForGroupBinding;
import com.example.chattingapp.databinding.FragmentChatBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class SelectPeopleForGroup extends AppCompatActivity {
    ActivitySelectPeopleForGroupBinding binding;

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    ArrayList<UserProfile> userShowArrayList;
    ArrayList<String>SelectedParticipants;
    String GroupId;
    ArrayList<String> groupmembersfinal,AlreadygroupMembers,Joined_Groups_arrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySelectPeopleForGroupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        groupmembersfinal=new ArrayList<>();
        firebaseAuth=FirebaseAuth.getInstance();
        Intent getGroupId=getIntent();
        GroupId=getGroupId.getStringExtra("GroupId");
        firebaseDatabase=FirebaseDatabase.getInstance();
        userShowArrayList=new ArrayList<>();
        Joined_Groups_arrayList=new ArrayList<>();
        AlreadygroupMembers=new ArrayList<>();
        SelectedParticipants=new ArrayList<>();
        binding.backArrowAddParticipants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
        groupSelectParticipantsAdapter GroupSelectParticipantsAdapter=new groupSelectParticipantsAdapter(this,userShowArrayList);
        binding.selectUserRecycleView.setAdapter(GroupSelectParticipantsAdapter);



        firebaseDatabase.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userShowArrayList.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    UserProfile userProfile=dataSnapshot.getValue(UserProfile.class);
                    //skipping the current useer to appear in list
                    if (userProfile.getUID().equals(FirebaseAuth.getInstance().getUid())) {
                        continue;
                    }else {
                        userShowArrayList.add(userProfile);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        firebaseDatabase.getReference().child("Groups").child(GroupId).child("groupMemebersList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                AlreadygroupMembers= (ArrayList<String>) snapshot.getValue();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.btnCheckAddParticipants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                groupmembersfinal.clear();
                SelectedParticipants=GroupSelectParticipantsAdapter.getSelectedParticipants();
                groupmembersfinal.addAll(AlreadygroupMembers);

                if(!SelectedParticipants.isEmpty()){
                    for (String memberPresent:SelectedParticipants){
                        if(!AlreadygroupMembers.contains(memberPresent)){
                            groupmembersfinal.add(memberPresent);
                        }

                    }



                    firebaseDatabase.getReference().child("Groups").child(GroupId).child("groupMemebersList").setValue(groupmembersfinal).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                //Adding Joined Groups
                                for(String memberId:groupmembersfinal){
                                    firebaseDatabase.getReference().child("Users").child(memberId).child("Joined_Groups").child(GroupId).setValue(GroupId);


                                }

                                //after task Completion
                                Toast.makeText(SelectPeopleForGroup.this, "Members Added Sucessfuly", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SelectPeopleForGroup.this, MainActivity.class));
                                finishAffinity();
                            }else{
                                Toast.makeText(SelectPeopleForGroup.this, "Failed to add members", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }else{
                    Toast.makeText(SelectPeopleForGroup.this, "Please Select Participants", Toast.LENGTH_SHORT).show();
                }






            }
        });

    }


}