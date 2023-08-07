FROM openjdk:19-alpine

WORKDIR /root

ENV LANG=C.UTF-8

RUN apk --no-cache add curl

COPY entrypoint.sh /root

COPY target/buy-oyc-catering-service.jar /root/buy-oyc-catering-service.jar

EXPOSE 8087

ENTRYPOINT ["/root/entrypoint.sh"]
