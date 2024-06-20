FROM eclipse-temurin:21
COPY target/todo-app-0.0.1-SNAPSHOT.jar todo-app-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/todo-app-0.0.1-SNAPSHOT.jar"]