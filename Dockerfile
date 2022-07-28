FROM openjdk:8-jdk-alpine

EXPOSE 8888

ADD target/ledger.jar app.jar

RUN bash -c 'touch /app.jar'

ENTRYPOINT ["java","-jar","/app.jar"]