FROM openjdk:17-jdk-alpine
LABEL authors="Anton_Lukyanau1"
VOLUME /tmp
ADD target/stocks-0.0.1-SNAPSHOT.jar stocks-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/stocks-0.0.1-SNAPSHOT.jar"]
