# RabbitMQ - SSL Example

SSL-Example for Python and Java Clients from https://www.rabbitmq.com/ssl.html.

## Docker - RabbitMQ-Server

Simple use docker-compose to create a docker rabbitmq container with ssl support.

```
cd Docker
docker-compose up
```

## RabbitMQ-Server Certificates

### SSL Certificate Generation

Rabbitmq ssl-tutorial --> https://www.rabbitmq.com/ssl.html

Using tls-gen's tool from michael klishin --> https://github.com/michaelklishin/tls-gen

```
git clone https://github.com/michaelklishin/tls-gen
cd tls-gen/basic
# private key password
make PASSWORD=bunnies
make verify
make info
```

```
ls -l ./result
# Files generated in result folder
--> ca_certificate.pem
--> ca_key.pem
--> client_certificate.pem
--> client_key.p12
--> client_key.pem
--> server_certificate.pem
--> server_key.p12
--> server_key.pem
```

### Create a Java Keystore File

https://docs.cloudera.com/documentation/enterprise/5-10-x/topics/cm_sg_openssl_jks.html

```
cd result

keytool.exe -importkeystore -srckeystore server_key.p12
  -srcstoretype PKCS12 -srcstorepass bunnies 
  -deststorepass bunnies -destkeypass password 
  -destkeystore server_store.jks
```


### SSL Certificate Setup

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

Old syntax by rabbitmq.config

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

New syntax by rabbitmq.conf

```
listeners.tcp = none
listeners.ssl.default = 5671

ssl_options.cacertfile           = /etc/ssl/rabbit/ca_certificate.pem
ssl_options.certfile             = /etc/ssl/rabbit/server_certificate.pem
ssl_options.keyfile              = /etc/ssl/rabbit/server_key.pem
ssl_options.verify               = verify_peer
ssl_options.fail_if_no_peer_cert = true
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

## Spring-Boot-amqp

RabbitMQ-SSL Spring-Boot example can be build and executed to use Gradle. Clients sends by
scheduled task every 5 seconds a sample message.

```
cd Spring
gradle bootRun
```

## License

Copyright 2017-2018 Andreas Sekulski

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
