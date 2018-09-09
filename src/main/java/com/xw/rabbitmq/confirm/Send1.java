package com.xw.rabbitmq.confirm;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.xw.rabbitmq.hello.ConnectionUtil;

public class Send1 {
    private static final String QUEUE_NAME = "test_queue_confirm1";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {

        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        //生产者调用confirmSelect 将channel设置为confirm模式
        //注意处于 transactional 模式的 channel 不能再被设置成 confirm 模式，反之亦然
        channel.confirmSelect();

        String msgString = "hello confirm message!";
        channel.basicPublish("", QUEUE_NAME, null, msgString.getBytes());

        if (!channel.waitForConfirms()) {
            System.out.println("message send failed");
        } else {
            System.out.println("message send ok");
        }

        channel.close();
        connection.close();
    }
}
