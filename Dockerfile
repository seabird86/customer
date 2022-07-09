FROM openjdk:17-alpine
WORKDIR /app
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} build/app.jar
WORKDIR /app/build
ENTRYPOINT ["java","-jar","app.jar"]