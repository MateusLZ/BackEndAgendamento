# Etapa de compilação
FROM ubuntu:latest AS build

RUN apt-get update && \
    apt-get clean &&\
    apt-get install -y openjdk-17-jdk maven && \
    apt-get clean


WORKDIR /app
COPY . .

RUN mvn clean install

# Etapa de execução
FROM openjdk:17-jdk-slim

EXPOSE 8080

COPY --from=build /app/target/produtos-0.0.1-SNAPSHOT.jar /app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
