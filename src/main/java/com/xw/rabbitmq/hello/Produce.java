package com.xw.rabbitmq.hello;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;

public class Produce {
    private final static String QUEUE_NAME = "com/xw/rabbitmq";

    public static void main(String[] argv) throws Exception {
        Connection connection = ConnectionUtil.getConnection();
//		创建一个频道
        Channel channel = connection.createChannel();
//		声明一个队列 -- 在RabbitMQ中，队列声明是幂等性的（一个幂等操作的特点是其任意多次执行所产生的影响均与一次执行的影响相同），
// 也就是说，如果不存在，就创建，如果存在，不会对已经存在的队列产生任何影响。
        //第二个参数为true，该队列会持久化
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        String message = "Hello World!";
//		发送消息到队列中
        channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));
        //这种简单的分发方式每条信息只能有一个读取

        System.out.println("P [x] Sent '" + message + "'");
//		关闭频道和连接
        channel.close();
        connection.close();
    }

}
