FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY target/WeatherBot-0.0.1-SNAPSHOT.jar /app/WeatherBot-0.0.1-SNAPSHOT.jar

CMD ["java", "-jar", "WeatherBot-0.0.1-SNAPSHOT.jar"]