FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY target/WeatherBot-0.0.1-SNAPSHOT.jar /app/WeatherBot-0.0.1-SNAPSHOT.jar

RUN mkdir -p /app/images

CMD ["java", "-jar", "WeatherBot-0.0.1-SNAPSHOT.jar"]