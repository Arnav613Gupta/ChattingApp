package com.example.chattingapp.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.chattingapp.Fragments.Calls_Fragment;
import com.example.chattingapp.Fragments.Chat_Fragment;
import com.example.chattingapp.Fragments.Group_Fragment;
import com.example.chattingapp.Fragments.Status_Fragment;

public class FragmentAdapter extends FragmentStateAdapter {
    public FragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:return new Group_Fragment();
            case 2:return new Calls_Fragment();
            case 3:return new Status_Fragment();
            default:return new Chat_Fragment();

        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
