package com.example.chattingapp.Model;

import android.net.Uri;

public class MessageModel {
    String messageUid,messageTxt,messageNodeId;
    long timestamp;
    String imagesend;


    public MessageModel() {
    }

    public MessageModel(String messageUid,String messageTxt, long timestamp) {
        this.messageUid = messageUid;
        this.timestamp = timestamp;
        this.messageTxt=messageTxt;
    }
    public MessageModel(String messageUid,String messageTxt,String imagesend, long timestamp) {
        this.messageUid = messageUid;
        this.timestamp = timestamp;
        this.imagesend=imagesend;
        this.messageTxt=messageTxt;
    }

    public MessageModel(String messageUid, String messageTxt, String messageNodeId, long timestamp, String imagesend) {
        this.messageUid = messageUid;
        this.messageTxt = messageTxt;
        this.messageNodeId = messageNodeId;
        this.timestamp = timestamp;
        this.imagesend = imagesend;
    }

    public String getMessageNodeId() {
        return messageNodeId;
    }

    public void setMessageNodeId(String messageNodeId) {
        this.messageNodeId = messageNodeId;
    }

    public String getMessageTxt() {
        return messageTxt;
    }

    public void setMessageTxt(String messageTxt) {
        this.messageTxt = messageTxt;
    }

    public String getMessageUid() {
        return messageUid;
    }

    public void setMessageUid(String messageUid) {
        this.messageUid = messageUid;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getImagesend() {
        return imagesend;
    }

    public void setImagesend(String imagesend) {
        this.imagesend = imagesend;
    }
}
