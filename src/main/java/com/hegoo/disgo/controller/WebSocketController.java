package com.hegoo.disgo.controller;

import com.alibaba.fastjson2.JSON;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hegoo.disgo.utils.Message;
import com.hegoo.disgo.utils.Result;
import com.hegoo.disgo.utils.ResultCode;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint(value = "/chatroom/{name}")
public class WebSocketController {
    private String name;
    private Session session;
    private static CopyOnWriteArraySet<WebSocketController> webSocketSet = new CopyOnWriteArraySet<>();
    private static Map<String, Session> sessionMap = new HashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("name") String name) {
        Result result = null;
        this.session = session;
        this.name = name;
        if (sessionMap.keySet().contains(name)) {
            result = new Result(ResultCode.UNAUTHORIZED);
            result.setData("Cannot establish multiple connections!");
            session.getAsyncRemote().sendText(JSON.toJSONString(result));
            return;
        }
        sessionMap.put(this.name, session);
        webSocketSet.add(this);
        System.out.println("User " + name + " joins the chatroom! Current online user size is " + webSocketSet.size());
        Message message = new Message(1, "System", "", "Welcome, " + this.name + "! We have " + webSocketSet.size() + " people in the chatroom right now!", sessionMap.keySet());
        result = Result.SUCCESS_WITH_DATA(message);
        for (Map.Entry<String, Session> entry : sessionMap.entrySet()) {
            entry.getValue().getAsyncRemote().sendText(JSON.toJSONString(result));
        }
    }

    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        sessionMap.remove(this.name);
        System.out.println("User " + this.name + " exits! Current online user size is " + webSocketSet.size());
        Result result = null;
        Message message = new Message(1, "System", "", "Goodbye " + this.name + "! We have " + webSocketSet.size() + " people in the chatroom now!", sessionMap.keySet());
        result = Result.SUCCESS_WITH_DATA(message);

        for (Map.Entry<String, Session> entry : sessionMap.entrySet()) {
            if (entry.getValue() != this.session) {
                entry.getValue().getAsyncRemote().sendText(JSON.toJSONString(result));
            }
        }
    }

    @OnMessage
    public void onMessage(String jsonMessage, Session session, @PathParam("name") String name) {
        System.out.println("From client " + name + ": " + jsonMessage);
        ObjectMapper objectMapper = new ObjectMapper();
        Message message = null;
        try {
            message = objectMapper.readValue(jsonMessage, Message.class);
            Result result = null;
            message.setSender(this.name);
            message.setOnlineList(sessionMap.keySet());
            if (message.getType() == 0) {
                Session senderSession = this.session;
                Session receiverSession = sessionMap.get(message.getReceiver());
                if (receiverSession != null) {
                    result = Result.SUCCESS_WITH_DATA(message);
                    senderSession.getAsyncRemote().sendText(JSON.toJSONString(result));
                    receiverSession.getAsyncRemote().sendText(JSON.toJSONString(result));
                } else {
                    result = new Result(ResultCode.USER_NOT_FOUND);
                    senderSession.getAsyncRemote().sendText(JSON.toJSONString(result));
                }
            } else {
                result = Result.SUCCESS_WITH_DATA(message);
                for (WebSocketController item : webSocketSet) {
                    item.session.getAsyncRemote().sendText(JSON.toJSONString(result));
                }
            }
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnError
    public void onError(Throwable error) {
        System.out.println("onError:" + error.getMessage());
    }
}
