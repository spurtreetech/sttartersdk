package com.spurtreetech.sttarter.lib.helper.models;

import java.util.ArrayList;

/**
 * Created by Shahbaz on 20-10-2016.
 */

public class BuzzArrayListModel {
    int status;
    ArrayList<BuzzModel> messages;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ArrayList<BuzzModel> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<BuzzModel> messages) {
        this.messages = messages;
    }
}
