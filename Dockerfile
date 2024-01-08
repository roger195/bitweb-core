FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY . .
RUN ./gradlew build -x test
EXPOSE 8080
CMD ["java", "-jar", "build/libs/core-0.0.1-SNAPSHOT.jar"]
