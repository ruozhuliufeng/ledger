FROM registry.cn-hangzhou.aliyuncs.com/ruozhuliufeng/open-jdk:8-jdk-alpine

EXPOSE 8888
ENV LANG C.UTF-8
ADD target/ledger.jar app.jar

CMD java -jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]