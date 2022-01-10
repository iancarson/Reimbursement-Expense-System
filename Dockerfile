FROM maven:3-jdk-8
ARG APP_ENV=dev
COPY pom.xml /tmp
COPY src /tmp/src
WORKDIR /tmp
RUN mvn package -DskipTests=true
CMD java -jar -Dspring.profiles.active=${APP_ENV} target/ers-0.0.1-SNAPSHOT.jar
