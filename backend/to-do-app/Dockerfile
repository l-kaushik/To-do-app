FROM maven:3.9.12-eclipse-temurin-25 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -Dmaven.test.skip=true
FROM eclipse-temurin:25-jre-noble
WORKDIR /app
COPY --from=build /app/target/todo_app.jar ./todo_app.jar
EXPOSE 8080
CMD ["java", "-jar", "/app/todo_app.jar"]