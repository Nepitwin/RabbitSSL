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

package com.example.spring.rabittssl.receiver;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Component;

/**
 * Receiver implementation to handle incoming messaging requests.
 * @author Andreas Sekulski
 */
@Component
public class DeviceMonitoringReceiver implements MessageListener {

    @Override
    public void onMessage(Message message) {
        System.out.println("Gained " + new String(message.getBody()));
        // Request can be handled by spring services...
    }
}
