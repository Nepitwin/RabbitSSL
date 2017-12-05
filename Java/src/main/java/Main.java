/*
 * Copyright 2017 Andreas Sekulski
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

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.net.URL;
import java.security.KeyStore;

/**
 * Simple messaging example with RabbitMQ SSL.
 * Guide from https://www.rabbitmq.com/ssl.html
 * @author Andreas Sekulski
 */
public class Main {

    /**
     * Client certificate as pkcs #12 file format.
     */
    private static final String CLIENT_CERTIFICATE = "keycert.p12";

    /**
     * Client password from certificate. This INFORMATION should be stored safely!!!!
     */
    private static final String CLIENT_PASSWORD_CERTIFICATE = "MySecretPassword";

    /**
     * Given file format from client certificates.
     */
    private static final String KEYSTORE_CLIENT = "PKCS12";

    /**
     * Server certificate as java keystore.
     */
    private static final String SERVER_CERTIFICATE = "rabbitstore.jks";

    /**
     * Password from java keystore. This INFORMATION should be stored safely!!!!
     */
    private static final String SERVER_CERTIFICATE_PASSWORD = "rabbitstore";

    /**
     * Java keystore type.
     */
    private static final String SERVER_CERTIFICATE_TYPE = "JKS";

    /**
     * TLS version which should be used.
     */
    private static final String TLS_TYPE = "TLSv1.2";

    /**
     * Rabbitmq server ip.
     */
    private static final String RABBIT_MQ_HOST = "127.0.0.1";

    /**
     * Rabbitmq port to listen.
     */
    private static final int RABBIT_MQ_PORT = 5671;

    /**
     * Rabbitmq user to login.
     */
    private static final String RABBIT_MQ_USER = "admin";

    /**
     * Password from rabbitmq user. This INFORMATION should be stored safely!!!!
     */
    private static final String RABBIT_MQ_PASSWORD = "admin";

    /**
     * Rabbitmq example channel to send and receive a message.
     */
    private static final String RABBIT_MQ_CHANNEL = "rabbitmq-java-ssl-test";

    public static void main(String[] args) throws Exception
    {
        URL serverCertificate = ClassLoader.getSystemClassLoader().getResource(SERVER_CERTIFICATE);
        URL clientCertificate = ClassLoader.getSystemClassLoader().getResource(CLIENT_CERTIFICATE);

        char[] keyPassphrase = CLIENT_PASSWORD_CERTIFICATE.toCharArray();
        KeyStore ks = KeyStore.getInstance(KEYSTORE_CLIENT);
        assert clientCertificate != null;
        ks.load(new FileInputStream(clientCertificate.getFile()), keyPassphrase);

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(ks, keyPassphrase);

        char[] trustPassphrase = SERVER_CERTIFICATE_PASSWORD.toCharArray();
        KeyStore tks = KeyStore.getInstance(SERVER_CERTIFICATE_TYPE);
        assert serverCertificate != null;
        tks.load(new FileInputStream(serverCertificate.getFile()), trustPassphrase);

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(tks);

        SSLContext c = SSLContext.getInstance(TLS_TYPE);
        c.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(RABBIT_MQ_HOST);
        factory.setPort(RABBIT_MQ_PORT);
        factory.setUsername(RABBIT_MQ_USER);
        factory.setPassword(RABBIT_MQ_PASSWORD);
        factory.useSslProtocol(c);

        Connection conn = factory.newConnection();
        Channel channel = conn.createChannel();

        //non-durable, exclusive, auto-delete queue
        channel.queueDeclare(RABBIT_MQ_CHANNEL, false, true, true, null);
        channel.basicPublish("", RABBIT_MQ_CHANNEL, null, "Hello SSL World :-)".getBytes());

        GetResponse chResponse = channel.basicGet(RABBIT_MQ_CHANNEL, false);
        if(chResponse == null) {
            System.out.println("No message retrieved");
        } else {
            byte[] body = chResponse.getBody();
            System.out.println("Received: " + new String(body));
        }

        channel.close();
        conn.close();
    }
}
