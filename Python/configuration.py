import pika
import ssl

ssl_opts = {
    "ca_certificate": "ca_certificate.pem",
    "client_certificate": "client_certificate.pem",
    "client_key": "client_key.pem",
    "cert_reqs": ssl.CERT_REQUIRED,
    "ssl_version": ssl.PROTOCOL_TLSv1_2
}

rabbit_opts = {
    "host": "localhost",
    "port": 5671,
    "user": "admin",
    "password": "admin",
}

rabbit_queue_opts = {
    "queue": "python_ssl",
    "message": "Hello SSL World :)"
}

context = ssl.create_default_context(cafile=ssl_opts["ca_certificate"])
context.load_cert_chain(ssl_opts["client_certificate"], ssl_opts["client_key"])
ssl_options = pika.SSLOptions(context, rabbit_opts["host"])
parameters = pika.ConnectionParameters(host=rabbit_opts["host"],
                                       port=rabbit_opts["port"],
                                       credentials=pika.PlainCredentials(rabbit_opts["user"], rabbit_opts["password"]),
                                       ssl_options=ssl_options)