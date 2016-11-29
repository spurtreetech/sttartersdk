package com.sttarter.unknown;

import com.sttarter.communicator.models.MessageModel;

import java.util.ArrayList;

/**
 * Created by Shahbaz on 20-10-2016.
 */

public class BuzzModel {

    String message_id;
    String client_id;
    MessageModel message;
    ArrayList<MessageModel> messageModel;

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public MessageModel getMessage() {
        return message;
    }

    public void setMessage(MessageModel message) {
        this.message = message;
    }

    public ArrayList<MessageModel> getMessageModel() {
        return messageModel;
    }

    public void setMessageModel(ArrayList<MessageModel> messageModel) {
        this.messageModel = messageModel;
    }
}
