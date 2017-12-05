FROM debian:stretch-slim

RUN apt-get update
RUN apt-get install nano rabbitmq-server -y

# SSL-Configuration
RUN mkdir /etc/ssl
RUN mkdir /etc/ssl/rabbit

ADD rabbitmq.config /etc/rabbitmq/
RUN chown rabbitmq:rabbitmq /etc/rabbitmq/rabbitmq.config

ADD cert.pem /etc/ssl/rabbit/
RUN chown rabbitmq:rabbitmq /etc/ssl/rabbit/cert.pem

ADD key.pem /etc/ssl/rabbit/
RUN chown rabbitmq:rabbitmq /etc/ssl/rabbit/key.pem

ADD cacert.pem /etc/ssl/rabbit/
RUN chown rabbitmq:rabbitmq /etc/ssl/rabbit/cacert.pem

# Define environment variables.
ENV RABBITMQ_USER admin
ENV RABBITMQ_PASSWORD admin
ADD init.sh /init.sh
RUN ["chmod", "+x", "/init.sh"]

EXPOSE 5671
CMD ["/init.sh"]
