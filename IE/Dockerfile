FROM maven:3.6.3-openjdk-8 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn clean package -DskipTests
COPY src ./src
RUN mvn clean

FROM openjdk:8-jdk-alpine
COPY --from=build /app/target/IE-1.1.jar /usr/local/lib/IE.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/usr/local/lib/IE.jar"]


