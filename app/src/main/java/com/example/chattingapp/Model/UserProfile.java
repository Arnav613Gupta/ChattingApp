package com.example.chattingapp.Model;

import java.util.ArrayList;

public class UserProfile {
    String UID,NAME,PHONENUMBER,PROFILEIMAGE;





    public UserProfile(String uid, String name, String phonenumber, String profileImage) {
        this.UID = uid;
        this.NAME = name;
        this.PHONENUMBER = phonenumber;
        this.PROFILEIMAGE = profileImage;
    }
    public UserProfile() {

    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getPHONENUMBER() {
        return PHONENUMBER;
    }

    public void setPHONENUMBER(String PHONENUMBER) {
        this.PHONENUMBER = PHONENUMBER;
    }
    public String getPROFILEIMAGE() {
        return PROFILEIMAGE;
    }

    public void setPROFILEIMAGE(String PROFILEIMAGE) {
        this.PROFILEIMAGE = PROFILEIMAGE;
    }


}
