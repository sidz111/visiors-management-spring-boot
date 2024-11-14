FROM openjdk:21
WORKDIR /app
COPY target/VMS-0.0.1-SNAPSHOT.jar /app/VMS-0.0.1-SNAPSHOT.jar

EXPOSE 8080

ENTRYPOINT [ "java", "-jar", "VMS-0.0.1-SNAPSHOT.jar" ]
