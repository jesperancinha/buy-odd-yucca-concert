FROM openjdk:17-slim-buster

WORKDIR /root

ENV LANG=C.UTF-8

RUN apt update -y

RUN apt install curl -y

COPY entrypoint.sh /root

COPY target/buy-oyc-api-service.jar /root/buy-oyc-api-service.jar

ENTRYPOINT ["/root/entrypoint.sh"]
