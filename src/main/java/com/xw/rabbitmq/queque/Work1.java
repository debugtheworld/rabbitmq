package com.xw.rabbitmq.queque;

import com.rabbitmq.client.*;
import com.xw.rabbitmq.hello.ConnectionUtil;

import java.io.IOException;
/**
 * 轮询分发 一个消费者拿一个处理 不考虑消费者消费能力 或者消费是否结束 两个消费者各一半
 *
 */
public class Work1 {
    private static final String TASK_QUEUE_NAME = "task_queue";

    public static void main(String[] argv) throws Exception {
        final Connection connection = ConnectionUtil.getConnection();
        final Channel channel = connection.createChannel();

        //这里不能用hello包下的hello队列，队列一经声明无法修改参数
        //第二个参数为true，该队列会持久化
        channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
        System.out.println("Worker1 [*] Waiting for messages.");

        final Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException, IOException {
                String message = new String(body, "UTF-8");

                System.out.println("Worker1 [x] Received '" + message + "'");
                try {
                    doWork(message);
                } finally {
                    System.out.println("Worker1 [x] Done");
                }
            }
        };
        //轮询分发 一个消费者拿一个处理 不考虑消费者消费能力 或者消费是否结束 两个消费者各一半

        //为true:一旦rabbitmq将消息分发给消费者，就会从内存在删除
        //为false：需要手动用 channel.basicAck(envelope.getDeliveryTag(), false);告诉rabbitmq我处理完了，可以删除了
        channel.basicConsume(TASK_QUEUE_NAME, true, consumer);
    }

    private static void doWork(String task) {
        try {
            Thread.sleep(1000); // 暂停1秒钟
        } catch (InterruptedException _ignored) {
            Thread.currentThread().interrupt();
        }
    }
}
