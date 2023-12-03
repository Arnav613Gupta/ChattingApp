package com.example.chattingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.chattingapp.Adapter.FragmentAdapter;
import com.example.chattingapp.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        fragment_tab_setup();
        getSupportActionBar().setElevation(0);









        
    }

    private void fragment_tab_setup(){
        FragmentAdapter fragmentAdapter=new FragmentAdapter(this);
        activityMainBinding.viewPagerMain.setAdapter(fragmentAdapter);
        String[]TabName={"Groups","Chats","Calls","Status"};
        new TabLayoutMediator(activityMainBinding.tabLayoutMain,activityMainBinding.viewPagerMain,((tab, position) -> tab.setText(TabName[position]))).attach();
        activityMainBinding.viewPagerMain.setCurrentItem(1);





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()==R.id.Settings_main){
            startActivity(new Intent(MainActivity.this,SettingActity.class));
           return true;
        } else if (item.getItemId()==R.id.newGrop_main) {
            startActivity(new Intent(MainActivity.this, Group_Name_Activity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}