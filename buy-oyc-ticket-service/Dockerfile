FROM openjdk:19-alpine

WORKDIR /root

ENV LANG=C.UTF-8

RUN apk --no-cache add curl

COPY entrypoint.sh /root

COPY target/buy-oyc-ticket-service.jar /root/buy-oyc-ticket-service.jar

EXPOSE 8084

ENTRYPOINT ["/root/entrypoint.sh"]
