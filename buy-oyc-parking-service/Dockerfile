FROM openjdk:17-slim-buster

WORKDIR /root

ENV LANG=C.UTF-8

COPY entrypoint.sh /root

COPY target/buy-oyc-parking-service.jar /root/buy-oyc-parking-service.jar

ENTRYPOINT ["/root/entrypoint.sh"]
