FROM ubuntu:latest AS build

RUN apt-get update
RUN apt-get install openjdk-17-jdk -y
COPY src/main/java/com/kaiquef30/peopleregistration/resources .

RUN apt-get install maven -y
RUN mvn clean install

FROM openjdk:17-slim

EXPOSE 8080

COPY --from=build /target/people-registration-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT [ "java", "-jar", "app.jar" ]