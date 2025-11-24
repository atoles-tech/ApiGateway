FROM eclipse-temurin:21-jdk-alpine

COPY /target/api_gateway-0.0.1-SNAPSHOT.jar /api-gateway/api-gateway.jar

WORKDIR /api-gateway

EXPOSE 8080

ENTRYPOINT [ "java","-jar","api-gateway.jar", "--spring.profiles.active=docker" ]