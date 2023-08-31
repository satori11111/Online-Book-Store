
FROM openjdk:17-jdk-slim
COPY target/*.jar online-book-store.jar
ENTRYPOINT ["java", "-jar", "online-book-store.jar"]
EXPOSE 8080
