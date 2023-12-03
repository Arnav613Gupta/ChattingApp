package com.example.chattingapp.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chattingapp.Chat_detail_Activity;
import com.example.chattingapp.Model.MessageModel;
import com.example.chattingapp.R;
import com.example.chattingapp.databinding.ReceiveChatLayoutBinding;
import com.example.chattingapp.databinding.ReceiveImageLayoutBinding;
import com.example.chattingapp.databinding.SelectReceiveChatLayoutBinding;
import com.example.chattingapp.databinding.SelectReceiveImageLayoutBinding;
import com.example.chattingapp.databinding.SelectSendChatLayoutBinding;
import com.example.chattingapp.databinding.SelectSendImgLayoutBinding;
import com.example.chattingapp.databinding.SendChatLayoutBinding;
import com.example.chattingapp.databinding.SendImgLayoutBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.HashSet;

public class Chat_message_recyclesAdapter extends RecyclerView.Adapter {
    Context context;
    FirebaseDatabase database;



    ArrayList<MessageModel> messagelist;
    final int ITEM_SENT=1;
    final int ITEM_RECEIVE=2;
    final int ITEM_PIC_SENT=3;
    final int ITEM_PIC_RECEIVE=4;





    public Chat_message_recyclesAdapter(Context context, ArrayList<MessageModel> messagelist) {
        this.context = context;
        this.messagelist = messagelist;
    }




    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        database=FirebaseDatabase.getInstance();
        if(viewType==ITEM_SENT){
            View view= LayoutInflater.from(context).inflate(R.layout.send_chat_layout,parent,false);
            return new sendViewHolder(view);
        } else if (viewType==ITEM_PIC_SENT) {
            View view=LayoutInflater.from(context).inflate(R.layout.send_img_layout,parent,false);
            return new senderImgViewHolder(view);

        } else if (viewType==ITEM_PIC_RECEIVE){
            View view=LayoutInflater.from(context).inflate(R.layout.receive_image_layout,parent,false);
            return new receiveImgViewHolder(view);
        }
        else {
            View view= LayoutInflater.from(context).inflate(R.layout.receive_chat_layout,parent,false);
            return new receiverViewHolder(view);

        }


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageModel message=messagelist.get(position);


        if(holder.getClass().equals(sendViewHolder.class)){
            ((sendViewHolder) holder).binding.txtsendMessage.setText(message.getMessageTxt());
        } else if (holder.getClass().equals(senderImgViewHolder.class)) {
            Picasso.get().load(message.getImagesend()).into(((senderImgViewHolder)holder).binding.imgsendmessage);

        } else if (holder.getClass().equals(receiveImgViewHolder.class)) {
            Picasso.get().load(message.getImagesend()).into(((receiveImgViewHolder)holder).binding.imgreceivemessage);

        } else  {
            ((receiverViewHolder)holder).binding.txtreceivemessage.setText(message.getMessageTxt());
        }


















    }

    @Override
    public int getItemCount() {
        return messagelist.size();
    }

    @Override
    public int getItemViewType(int position) {
        MessageModel message=messagelist.get(position);

        if(message.getMessageUid().equals(FirebaseAuth.getInstance().getUid())){
            //text message send
            if (!message.getMessageTxt().equals("")){

                return ITEM_SENT;

            } else  {

                    return ITEM_PIC_SENT;


            }
        }else  {
            if (!message.getMessageTxt().equals("")){
                    return ITEM_RECEIVE;


            }else {

                    return ITEM_PIC_RECEIVE;



        }
        }

    }

    public class sendViewHolder extends RecyclerView.ViewHolder {
        SendChatLayoutBinding binding;
        public sendViewHolder(@NonNull View itemView) {
            super(itemView);
            binding=SendChatLayoutBinding.bind(itemView);

        }
    }

    public class receiverViewHolder extends RecyclerView.ViewHolder {
        ReceiveChatLayoutBinding binding;
        public receiverViewHolder(@NonNull View itemView) {
            super(itemView);
            binding=ReceiveChatLayoutBinding.bind(itemView);
        }
    }
    public class senderImgViewHolder extends RecyclerView.ViewHolder{
        SendImgLayoutBinding binding;

        public senderImgViewHolder(@NonNull View itemView) {
            super(itemView);
            binding=SendImgLayoutBinding.bind(itemView);
        }
    }
    public class receiveImgViewHolder extends RecyclerView.ViewHolder{
        ReceiveImageLayoutBinding binding;

        public receiveImgViewHolder(@NonNull View itemView) {
            super(itemView);
            binding=ReceiveImageLayoutBinding.bind(itemView);
        }
    }


}





