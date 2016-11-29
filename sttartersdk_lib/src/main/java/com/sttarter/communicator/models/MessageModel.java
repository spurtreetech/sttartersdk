package com.sttarter.communicator.models;

import com.sttarter.common.models.PayloadModel;

import java.util.ArrayList;

/**
 * Created by Shahbaz on 20-10-2016.
 */

public class MessageModel {

    String type;
    int timestamp;
    String from;
    String file_type;
    String file_url;
    PayloadModel payload;
    ArrayList<PayloadModel> payloadModel;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFile_type() {
        return file_type;
    }

    public void setFile_type(String file_type) {
        this.file_type = file_type;
    }

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    public PayloadModel getPayload() {
        return payload;
    }

    public void setPayload(PayloadModel payload) {
        this.payload = payload;
    }

    public ArrayList<PayloadModel> getPayloadModel() {
        return payloadModel;
    }

    public void setPayloadModel(ArrayList<PayloadModel> payloadModel) {
        this.payloadModel = payloadModel;
    }
}
