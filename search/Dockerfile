FROM openjdk:11-jdk
VOLUME /tmp
COPY target/search-1.0.jar Search.jar
ENTRYPOINT ["java", "-jar", "Search.jar"]