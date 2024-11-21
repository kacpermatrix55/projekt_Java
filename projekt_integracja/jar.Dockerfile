FROM gradle:8.1.1-jdk17 AS builder
WORKDIR /data
COPY . /data
COPY src/main/resources/application.properties.docker /data/src/main/resources/application.properties
#RUN gradle genJaxb
RUN gradle assemble

FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY --from=builder /data/build/libs/demo-0.0.1-SNAPSHOT.jar /app/myapp.jar
CMD java -jar /app/myapp.jar
