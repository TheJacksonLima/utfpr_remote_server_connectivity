FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

RUN ls -la / && ls -la /app

COPY target/QuoteAPI-1.0.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
