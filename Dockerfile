FROM --platform=linux/amd64 openjdk:11-jdk
ARG JAR_FILE=build/libs/yeon-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} wooyeon.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "wooyeon.jar"]