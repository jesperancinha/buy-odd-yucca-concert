FROM openjdk:19-alpine

WORKDIR /root

ENV LANG=C.UTF-8

RUN apk --no-cache add curl

COPY entrypoint.sh /root

COPY target/buy-oyc-concert-service.jar /root/buy-oyc-concert-service.jar

EXPOSE 8085

ENTRYPOINT ["/root/entrypoint.sh"]
