FROM openjdk:17-jdk-slim-buster
WORKDIR /app

COPY build/libs/* build/lib/

COPY build/libs/dotaslz*.jar build/

WORKDIR /app/build
ENTRYPOINT java -jar dotaslz-0.0.1-SNAPSHOT.jar