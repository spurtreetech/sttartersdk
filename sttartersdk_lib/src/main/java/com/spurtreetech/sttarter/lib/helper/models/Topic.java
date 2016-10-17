package com.spurtreetech.sttarter.lib.helper.models;

import java.util.ArrayList;

/**
 * Created by RahulT on 18-06-2015.
 */
public class Topic {

    int id, is_public;
    String app_id, topic, type, users, created_by, created_at, updated_at;
    //TopicMeta meta;
    boolean active;
    TopicMeta meta;
    ArrayList<Group_user_data> group_members;

    public ArrayList<Group_user_data> getGroup_members() {
        return group_members;
    }

    public void setGroup_members(ArrayList<Group_user_data> group_members) {
        this.group_members = group_members;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getIs_public() {
        return is_public;
    }

    public void setIs_public(int is_public) {
        this.is_public = is_public;
    }


    public TopicMeta getMeta() {
        return meta;
    }

    public void setMeta(TopicMeta meta) {
        this.meta = meta;
    }

}
