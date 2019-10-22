FROM openjdk:8-jdk-alpine
MAINTAINER astrodix@gmail.com

COPY ./ ./

RUN ./mvnw clean package

ENTRYPOINT ["java","-jar","./target/comparator-1.0.jar"]