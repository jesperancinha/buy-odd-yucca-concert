FROM openjdk:17-slim-buster

WORKDIR /root

ENV LANG=C.UTF-8

COPY entrypoint.sh /root

COPY target/buy-oyc-concert-service.jar /root/buy-oyc-concert-service.jar

ENTRYPOINT ["/root/entrypoint.sh"]
