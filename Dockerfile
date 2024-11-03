FROM openjdk:21
COPY build/libs/stateless-auth-api-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]