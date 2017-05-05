FROM openjdk:8-jre-alpine

RUN apk update
RUN apk upgrade

RUN apk add bash

ENV JAVA_HOME /usr/lib/jvm/java-8-*/
ENV PORT 8080
ENV ADMIN_PORT 8081

EXPOSE 8080
EXPOSE 8081

WORKDIR /app

ADD target/config.yml /app/
ADD target/original-dropwizard-guice-*.jar /app/
ADD docker-start.sh /app/docker-start.sh

CMD bash ./docker-start.sh