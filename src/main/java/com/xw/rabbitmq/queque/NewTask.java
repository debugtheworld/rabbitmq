package com.xw.rabbitmq.queque;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;
import com.xw.rabbitmq.hello.ConnectionUtil;

public class NewTask {
    private static final String TASK_QUEUE_NAME = "task_queue";

    public static void main(String[] argv) throws java.io.IOException, Exception {

        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();

        //这里不能用hello包下的hello队列，队列一经声明无法修改参数
        //第二个参数为true，该队列会持久化
        channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
//      分发消息
        for (int i = 0; i < 20; i++) {
            String message = "Hello World! " + i;
            channel.basicPublish("", TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
        }
        channel.close();
        connection.close();
    }
}
