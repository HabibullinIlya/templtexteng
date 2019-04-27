FROM openjdk:8-stretch
VOLUME /tmp
ARG JAR_FILE=./build/libs/templatesengine-0.0.1-SNAPSHOT.war
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]
