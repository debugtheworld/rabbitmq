package com.xw.rabbitmq.fairqueues;

import com.rabbitmq.client.*;
import com.xw.rabbitmq.hello.ConnectionUtil;

import java.io.IOException;

public class Work2 {
    private static final String TASK_QUEUE_NAME = "task_queue";

    public static void main(String[] argv) throws Exception {
        final Connection connection = ConnectionUtil.getConnection();
        final Channel channel = connection.createChannel();

        //这里不能用hello包下的hello队列，队列一经声明无法修改参数
        //第二个参数为true，该队列会持久化
        channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
        System.out.println("Worker2 [*] Waiting for messages.");


        /**
         * 公平分发
         * 每个消费者 发送确认消息之前,消息队列不发送下一个消息到消费者,一次只处理一个消息
         *
         * 限制发送给同一个消费者 不得超过一条消息
         */
        channel.basicQos(1);

        final Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");

                System.out.println("Worker2 [x] Received '" + message + "'");
                try {
                    doWork(message);
                } finally {
                    System.out.println("Worker2 [x] Done");
                    // 消息处理完成确认     自动应答改成false
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };
        // 消息消费完成确认
        //为true:一旦rabbitmq将消息分发给消费者，就会从内存在删除
        //为false：需要手动用 channel.basicAck(envelope.getDeliveryTag(), false);告诉rabbitmq我处理完了，可以删除了
        channel.basicConsume(TASK_QUEUE_NAME, false, consumer);
    }

    /**
     * 任务处理
     *
     * @param task
     *            void
     */
    private static void doWork(String task) {
        try {
            Thread.sleep(2000); // 暂停1秒钟
        } catch (InterruptedException _ignored) {
            Thread.currentThread().interrupt();
        }
    }
}
