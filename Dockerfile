FROM openjdk:8-jdk-alpine

EXPOSE 8888

ADD target/ledger.jar app.jar

CMD java -jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]