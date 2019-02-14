package com.andy.service.impl;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

import java.nio.charset.Charset;

public class rabbitMQServiceImpl implements MessageListener {
    @Override
    public void onMessage(Message message) {
        String msgBody = new java.lang.String(message.getBody(), Charset.forName("utf-8"));
        System.out.println("消息消费者 = " + msgBody);
    }
}
