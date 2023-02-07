package com.hegoo.disgo.utils;

import java.util.Set;

public class Message {
    private int type; //聊天类型0：私聊，1：广播，2：加入成功
    private String sender;//发送者.
    private String receiver;//接受者.
    private String msg;//消息
    private Set<String> onlineList;//在线列表

    public Message() {
    }

    public Message(int type, String sender, String receiver, String msg, Set<String> onlineList) {
        this.type = type;
        this.sender = sender;
        this.receiver = receiver;
        this.msg = msg;
        this.onlineList = onlineList;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Set<String> getOnlineList() {
        return onlineList;
    }

    public void setOnlineList(Set<String> onlineList) {
        this.onlineList = onlineList;
    }
}
