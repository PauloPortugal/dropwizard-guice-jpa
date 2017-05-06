#!/bin/bash
mvn -DskipTests clean package && \
docker build -t pauloportugal/dropwizard-guice-jpa:local . && \
docker run -ti --rm --name guice-jpa pauloportugal/dropwizard-guice-jpa:local