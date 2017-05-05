#!/bin/bash
mvn -DskipTests clean package && docker build -t pauloportugal/dropwizard-guice-jpa:local .