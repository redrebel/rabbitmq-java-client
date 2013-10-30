package com.rabbitmq.client.test;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.impl.AMQConnection;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SharedThreadPoolTest extends BrokerTestCase {
    public void testSharedThreadPool() throws IOException {
        ConnectionFactory cf = new ConnectionFactory();
        ExecutorService executor = Executors.newFixedThreadPool(8);
        cf.setSharedExecutor(executor);

        AMQConnection conn1 = (AMQConnection)cf.newConnection();
        assertFalse(conn1.willShutDownConsumerExecutor());

        AMQConnection conn2 = (AMQConnection)cf.newConnection();
        assertFalse(conn2.willShutDownConsumerExecutor());

        AMQConnection conn3 = (AMQConnection)cf.newConnection(Executors.newSingleThreadExecutor());
        assertFalse(conn3.willShutDownConsumerExecutor());

        assertSame(conn1.getConsumerExecutor(), conn2.getConsumerExecutor());
        assertNotSame(conn2.getConsumerExecutor(), conn3.getConsumerExecutor());
    }

    public void testWillShutDownExecutor() throws IOException {
        ConnectionFactory cf = new ConnectionFactory();
        ExecutorService executor = Executors.newFixedThreadPool(8);
        cf.setSharedExecutor(executor);

        AMQConnection conn1 = (AMQConnection)cf.newConnection();
        assertFalse(conn1.willShutDownConsumerExecutor());

        AMQConnection conn2 = (AMQConnection)cf.newConnection();
        assertFalse(conn2.willShutDownConsumerExecutor());

        AMQConnection conn3 = (AMQConnection)cf.newConnection(Executors.newSingleThreadExecutor());
        assertFalse(conn3.willShutDownConsumerExecutor());

        AMQConnection conn4 = (AMQConnection)cf.newConnection((ExecutorService)null);
        assertTrue(conn4.willShutDownConsumerExecutor());
    }
}
