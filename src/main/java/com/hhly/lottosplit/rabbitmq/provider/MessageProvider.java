package com.hhly.lottosplit.rabbitmq.provider;

/**
 * 发送消息到队列
 *
 * @author wulong
 * @create 2017/5/11 17:14
 */
public interface MessageProvider {
    /**
     * 发送消息
     * @param queueKey 队列名
     * @param message  消息
     */
    public void sendMessage(String queueKey,Object message);
}
