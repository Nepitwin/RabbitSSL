# RabbitMQ - SSL Example

SSL-Example for Python and Java Clients from https://www.rabbitmq.com/ssl.html.

## Docker - RabbitMQ-Server

If you don't want create your own RabbitMQ-Server a docker container can be used. Build and Start a sample RabbitMQ TLS Docker Container to test TLS-Messaging.

```
cd Docker
docker build --rm=true --tag=ssl/rabbit .
docker run -d -p 5671:5671 ssl/rabbit
```

## RabbitMQ-Server Certificates Setup

Copy /Certificates/testca/cacert.pem, /Certificates/server/key.pem and /Certificates/server/cert.pem to /etc/ssl/rabbit/ to setup certificate example files to establish a RabbitMQ-TLS connection.

```
cd /etc/ssl/
mkdir rabbit
cd <GIT_Project_PATH>
cp /Certificates/testca/cacert.pem /etc/ssl/rabbit/cacert.pem
cp /Certificates/server/cert.pem /etc/ssl/rabbit/cert.pem
cp /Certificates/server/key.pem /etc/ssl/rabbit/key.pem
```

### SSL-Configuration

Copy this example file for RabbiMQ and restart service.

```
[
  {rabbit, [
     {ssl_listeners, [5671]},
     {ssl_options, [{cacertfile,"/etc/ssl/rabbit/cacert.pem"},
                    {certfile,"/etc/ssl/rabbit/cert.pem"},
                    {keyfile,"/etc/ssl/rabbit/key.pem"},
                    {verify,verify_peer},
                    {fail_if_no_peer_cert,true}]}
   ]}
].
```

## Execute Examples

Use Java (Gradle) or Python examples to execute examples.

### Python

Python client is split up for a consumer (rabitssl_consume.py) and producer (rabitssql_send.py). Producer sends a message to RabbitMQ-Server and consumer receives this message. PIKA is needed to establish connections.

```
cd Python
python rabitssql_consume.py/rabitssql_send.py
```

### Java

Java client sends and receives message from a single main file. Simple use Gradle to build and run example.

```
cd Java
gradle run
```

## License

Copyright 2017 Andreas Sekulski

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
