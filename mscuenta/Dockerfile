FROM eclipse-temurin:21-jre-alpine

LABEL maintainer="Obed"

RUN addgroup -S spring && adduser -S spring -G spring

USER spring:spring

VOLUME /tmp

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app.jar"]