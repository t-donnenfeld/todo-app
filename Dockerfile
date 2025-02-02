ARG BUILD_HOME=/app

FROM gradle:8.12 AS build-image

ARG BUILD_HOME
ENV APP_HOME=$BUILD_HOME
WORKDIR $APP_HOME

COPY --chown=gradle:gradle build.gradle settings.gradle $APP_HOME/
COPY --chown=gradle:gradle src $APP_HOME/src

RUN gradle --no-daemon build


FROM openjdk:21-jdk

ARG BUILD_HOME
ENV APP_HOME=$BUILD_HOME

ENV DB_DRIVER="org.h2.Driver"
ENV DB_URL="jdbc:h2:file:./mydb"
ENV DB_USERNAME="user"
ENV DB_PASSWORD="password"

COPY --from=build-image $APP_HOME/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]