package com.hhly.lottosplit.rabbitmq.provider.impl;

import java.util.Random;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hhly.lottosplit.rabbitmq.provider.MessageProvider;

/**
 * 发送消息
 *
 * @author wulong
 * @create 2017/5/11 17:08
 */
@Service
public class SendTicketMessageProvider implements MessageProvider {

    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * 发送消息
     *
     * @param queueKey 队列名
     * @param message  消息
     */
    @Override
    public void sendMessage(String queueKey, Object message) {
        byte [] body= message.toString().getBytes();
        MessageProperties properties = new MessageProperties();
        properties.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
        properties.setPriority(new Random().nextInt(10));
        Message message2 = new Message(body,properties );
        amqpTemplate.send(queueKey,message2);
    }
}