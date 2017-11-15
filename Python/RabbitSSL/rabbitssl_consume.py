import pika
import ssl

ssl_opts = {
    "ca_certs":"cacert.pem",
    "certfile": "cert.pem",
    "keyfile": "key.pem",
    "cert_reqs": ssl.CERT_REQUIRED,
    "ssl_version": ssl.PROTOCOL_TLSv1_2
}

parameters = pika.ConnectionParameters(host='127.0.0.1',
                                       port=5671,
                                       credentials=pika.PlainCredentials('guest', 'guest'),
                                       ssl=True,
                                       ssl_options=ssl_opts)

try:
    connection = pika.BlockingConnection(parameters)
    channel = connection.channel()

    channel.queue_declare(queue='hello')

    def callback(ch, method, properties, body):
        print(" [x] Received %r" % body)


    channel.basic_consume(callback,
                          queue='hello',
                          no_ack=True)

    print(' [*] Waiting for messages. To exit press CTRL+C')
    channel.start_consuming()

except BaseException as e:
    print(str(e), e.__class__.__name__)
