FROM openjdk:12-alpine
COPY .build/libs/plugtify-0.0.1-SNAPSHOT.jar /usr/app/
WORKDIR /usr/app
ENTRYPOINT ["java", "-jar", "plugtify-0.0.1-SNAPSHOT.jar"]
