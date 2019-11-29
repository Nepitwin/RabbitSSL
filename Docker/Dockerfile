FROM debian:stretch-slim

RUN apt-get update
RUN apt-get install rabbitmq-server -y

# SSL-Configuration
RUN mkdir /etc/ssl
RUN mkdir /etc/ssl/rabbit

ADD rabbitmq.config /etc/rabbitmq/
RUN chown rabbitmq:rabbitmq /etc/rabbitmq/rabbitmq.config

ADD ca_certificate.pem /etc/ssl/rabbit/
RUN chown rabbitmq:rabbitmq /etc/ssl/rabbit/ca_certificate.pem

ADD server_certificate.pem /etc/ssl/rabbit/
RUN chown rabbitmq:rabbitmq /etc/ssl/rabbit/server_certificate.pem

ADD server_key.pem /etc/ssl/rabbit/
RUN chown rabbitmq:rabbitmq /etc/ssl/rabbit/server_key.pem

# Define environment variables.
ENV RABBITMQ_USER admin
ENV RABBITMQ_PASSWORD admin
ADD init.sh /init.sh
RUN ["chmod", "+x", "/init.sh"]

EXPOSE 5671
CMD ["/init.sh"]
