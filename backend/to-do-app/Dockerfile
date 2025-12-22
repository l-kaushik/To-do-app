FROM eclipse-temurin:25
RUN mkdir /app
COPY target/todo_app.jar /app
EXPOSE 8080
CMD ["java", "-jar", "/app/todo_app.jar"]