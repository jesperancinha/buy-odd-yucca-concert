FROM openjdk:17-slim-buster

WORKDIR /root

ENV LANG=C.UTF-8

COPY entrypoint.sh /root

COPY target/buy-oyc-catering-service.jar /root/buy-oyc-catering-service.jar

EXPOSE 8087

ENTRYPOINT ["/root/entrypoint.sh"]
