package com.example.chattingapp.Model;

import java.util.ArrayList;

public class GroupModel {
    String groupImg,createTime,groupName,lastUpdated,groupUID;
    ArrayList<String> groupMemebersList;
    ArrayList<MessageModel>groupMessages;


    public GroupModel() {
    }

    public String getGroupImg() {
        return groupImg;
    }

    public String getGroupUID() {
        return groupUID;
    }

    public void setGroupUID(String groupUID) {
        this.groupUID = groupUID;
    }

    public void setGroupImg(String groupImg) {
        this.groupImg = groupImg;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public ArrayList<String> getGroupMemebersList() {
        return groupMemebersList;
    }

    public void setGroupMemebersList(ArrayList<String> groupMemebersList) {
        this.groupMemebersList = groupMemebersList;
    }

    public ArrayList<MessageModel> getGroupMessages() {
        return groupMessages;
    }

    public void setGroupMessages(ArrayList<MessageModel> groupMessages) {
        this.groupMessages = groupMessages;
    }
}
