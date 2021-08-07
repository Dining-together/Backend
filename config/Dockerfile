FROM openjdk:11-jdk
VOLUME /tmp
COPY target/config-1.0.jar Config.jar
ENTRYPOINT ["java", "-jar", "Config.jar"]