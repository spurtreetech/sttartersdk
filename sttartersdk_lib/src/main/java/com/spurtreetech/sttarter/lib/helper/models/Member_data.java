package com.spurtreetech.sttarter.lib.helper.models;

/**
 * Created by Shahbaz on 9/11/2015.
 */
public class Member_data {

    private int stt_id;
    private String name;
    private String username;
    private String email;
    private String mobile;
    private String avatar;
    private String meta;
    private boolean is_active;
    private boolean is_approved;

    public boolean is_approved() {
        return is_approved;
    }

    public void setIs_approved(boolean is_approved) {
        this.is_approved = is_approved;
    }

    public int getStt_id() {
        return stt_id;
    }

    public void setStt_id(int stt_id) {
        this.stt_id = stt_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public boolean is_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }





}
