
FROM openjdk:17-jdk-slim
COPY target/Online-Book-Store-0.0.1-SNAPSHOT.jar online-book-store.jar
ENTRYPOINT ["java", "-jar", "online-book-store.jar"]
EXPOSE 8080
