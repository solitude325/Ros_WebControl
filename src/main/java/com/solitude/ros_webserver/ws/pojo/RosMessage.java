package com.solitude.ros_webserver.ws.pojo;

import lombok.Data;

@Data
public class RosMessage {
    private String op;
    private String topic;
    private String msg;
}
