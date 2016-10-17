package com.spurtreetech.sttarter.lib.helper.models;

/**
 * Created by rahul on 06/08/15.
 */
public class TopicMeta {

    String name;
    String allow_reply;
    String is_public;
    String email;
    String image;
    String group_desc;
    String share_contacts;
    String username;
    String mobile;
    String avatar;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }



    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAllow_reply() {
        return allow_reply;
    }

    public void setAllow_reply(String allow_reply) {
        this.allow_reply = allow_reply;
    }

    public String getIs_public() {
        return is_public;
    }

    public void setIs_public(String is_public) {
        this.is_public = is_public;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGroup_desc() {
        return group_desc;
    }

    public void setGroup_desc(String group_desc) {
        this.group_desc = group_desc;
    }

    public String getShare_contacts() {
        return share_contacts;
    }

    public void setShare_contacts(String share_contacts) {
        this.share_contacts = share_contacts;
    }

}
