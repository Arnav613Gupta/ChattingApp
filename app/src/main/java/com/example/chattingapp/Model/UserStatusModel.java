package com.example.chattingapp.Model;

import java.util.ArrayList;

public class UserStatusModel {

    private String name,profileImage,userUid;
    private long  lastUpdated;
    private ArrayList<StatusImg> statusImgArrayList;

    public UserStatusModel() {
    }

    public UserStatusModel(String name, String profileImage, long lastUpdated, ArrayList<StatusImg> statusImgArrayList) {
        this.name = name;
        this.profileImage = profileImage;
        this.lastUpdated = lastUpdated;
        this.statusImgArrayList = statusImgArrayList;
    }

    public String getUserUid() {
        return userUid;
    }

    public UserStatusModel(String name, String profileImage, String userUid, long lastUpdated, ArrayList<StatusImg> statusImgArrayList) {
        this.name = name;
        this.profileImage = profileImage;
        this.userUid = userUid;
        this.lastUpdated = lastUpdated;
        this.statusImgArrayList = statusImgArrayList;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public ArrayList<StatusImg> getStatusImgArrayList() {
        return statusImgArrayList;
    }

    public void setStatusImgArrayList(ArrayList<StatusImg> statusImgArrayList) {
        this.statusImgArrayList = statusImgArrayList;
    }
}
