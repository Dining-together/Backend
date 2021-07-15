FROM openjdk:11-jdk
VOLUME /tmp
COPY target/member-1.0.jar Member.jar
ENTRYPOINT ["java", "-jar", "Member.jar"]