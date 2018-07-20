/*
 * Copyright 2017-2018 Andreas Sekulski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.spring.rabittssl.client;

import com.example.spring.rabittssl.configuration.RabbitConfiguration;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * RabbitClient implementation to send sample data to channel.
 * @author Andreas Sekulski.
 */
@Component
public class RabbitClient {

    /**
     * Connection factory to establish rabbitmq connections.
     */
    @Autowired
    @Qualifier("GEO_RABBIT_CONNECTION")
    private ConnectionFactory factory;

    /**
     * Scheduled message task to send sample data to channel.
     * @throws IOException      Exception if wrong parameters are used for connection.
     * @throws TimeoutException If duration from sending is too long.
     */
    @Scheduled(fixedRate = 5000)
    public void sendMessageToChannel() throws IOException, TimeoutException {
        String channelName = RabbitConfiguration.DEFAULT_QUEUE;
        String message = "Message :-)";

        Connection connection = factory.createConnection();
        Channel channel = connection.createChannel(false);
        channel.queueDeclare(channelName, false, false, false, null);
        channel.basicPublish("", channelName, null, message.getBytes());
        channel.close();
        connection.close();
    }
}
