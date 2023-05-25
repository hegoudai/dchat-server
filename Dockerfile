FROM gradle:7.6.1-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle buildFatJar --no-daemon

FROM openjdk:17
EXPOSE 80:80
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/dchat-server.jar
ENTRYPOINT ["java","-jar","/app/dchat-server.jar"]