FROM openjdk:19-alpine

WORKDIR /root

ENV LANG=C.UTF-8

RUN apk --no-cache add curl

COPY entrypoint.sh /root

COPY target/buy-oyc-api-service.jar /root/buy-oyc-api-service.jar

EXPOSE 8088

ENTRYPOINT ["/root/entrypoint.sh"]
