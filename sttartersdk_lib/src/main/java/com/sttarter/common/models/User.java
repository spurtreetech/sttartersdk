package com.sttarter.common.models;

/**
 * Created by Shahbaz on 9/11/2015.
 */
public class User {

    private int stt_id;
    private String name;
    private String username;
    private String email;
    private String mobile;
    private String avatar;
    private String meta;
    private String master_topic;
    private String org_topic;
    private String user_token;

    private boolean is_active;
    private boolean is_approved;

    private String dateOfBirth;
    private String password;


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

    public String getMaster_topic() {
        return master_topic;
    }

    public void setMaster_topic(String master_topic) {
        this.master_topic = master_topic;
    }

    public String getOrg_topic() {
        return org_topic;
    }

    public void setOrg_topic(String org_topic) {
        this.org_topic = org_topic;
    }

    public boolean is_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public boolean is_approved() {
        return is_approved;
    }

    public void setIs_approved(boolean is_approved) {
        this.is_approved = is_approved;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser_token() {
        return user_token;
    }

    public void setUser_token(String user_token) {
        this.user_token = user_token;
    }
}
