FROM ubuntu:latest As build

RUN apt-get update && \
    apt-get install -y openjdk-17-jdk maven && \
    apt-get clean

    WORKDIR /app
    COPY . .
    
    RUN mvn -f /pom.xml clean install
     
    FROM openjdk:17-jdk-slim
    
    EXPOSE 8080
    
    COPY --from=build /app/target/produtos-0.0.1-SNAPSHOT.jar /app.jar
    
    ENTRYPOINT ["java", "-jar", "/app.jar"]