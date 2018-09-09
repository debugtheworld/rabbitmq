package com.xw.rabbitmq.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.xw.rabbitmq.hello.ConnectionUtil;

//注意
//交换器在匹配模式下：
//
//如果消费者端的路由关键字只使用【#】来匹配消息，在匹配【topic】模式下，它会变成一个分发【fanout】模式，接收所有消息。
//
//如果消费者端的路由关键字中没有【#】或者【*】，它就变成直连【direct】模式来工作。


//匹配交换器的匹配符
//
//*（星号）表示一个单词
//#（井号）表示零个或者多个单词
public class TopicSendDirect {
    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] argv) throws java.io.IOException, Exception {

        Connection connection = ConnectionUtil.getConnection();
        Channel channel = connection.createChannel();


//			声明一个匹配模式的交换器
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");

        // 待发送的消息
        String[] routingKeys = new String[]{"quick.orange.rabbit",
                "lazy.orange.elephant",
                "quick.orange.fox",
                "lazy.brown.fox",
                "quick.brown.fox",
                "quick.orange.male.rabbit",
                "lazy.orange.male.rabbit"};
//			发送消息
        for (String severity : routingKeys) {
            String message = "From " + severity + " routingKey' s message!";
            channel.basicPublish(EXCHANGE_NAME, severity, null, message.getBytes());
            System.out.println("TopicSend [x] Sent '" + severity + "':'" + message + "'");
        }
    }
}
