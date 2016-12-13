package com.sttarter.communicator.models;

import com.sttarter.common.responses.STTResponse;

/**
 * Created by Shahbaz on 12-12-2016.
 */

public class CreateGroupResponse extends STTResponse {

    GroupStringMeta topic;

    public GroupStringMeta getTopic() {
        return topic;
    }

    public void setTopic(GroupStringMeta topic) {
        this.topic = topic;
    }
}
