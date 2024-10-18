FROM openjdk:17-jdk-slim

COPY build/libs/project-service-0.0.1-SNAPSHOT.jar /crewup-service.jar

ENTRYPOINT ["java", "-jar", "/crewup-service.jar"]