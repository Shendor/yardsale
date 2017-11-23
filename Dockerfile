FROM gradle:4.2-jdk8 as build
ENV GRADLE_USER_HOME /home/gradle
USER root
COPY . .
RUN ls
RUN gradle build

FROM java:8u111-jre-alpine
WORKDIR /yardsale-app
COPY --from=build /home/gradle/build/libs/yardsale-1.0-SNAPSHOT.jar /yardsale-app
CMD ["java", "-jar", "yardsale-1.0-SNAPSHOT.jar"]