package com.xw.rabbitmq.hello;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConnectionUtil {
    public static Connection getConnection() throws IOException, TimeoutException {
        // 创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
//		设置RabbitMQ地址
        factory.setHost("47.101.32.189");
        //默认端口是5672
        //factory.setPort(5672);
        factory.setUsername("xw");
        factory.setPassword("listen2you");
//		创建一个新的连接
        Connection connection = factory.newConnection();
        return connection;
    }
}
