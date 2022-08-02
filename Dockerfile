FROM registry.cn-hangzhou.aliyuncs.com/ruozhuliufeng/open-jdk:8-jdk-alpine

EXPOSE 8888 5005
ENV LANG C.UTF-8
ADD target/ledger.jar app.jar

#解决字体问题
RUN echo -e "https://mirror.tuna.tsinghua.edu.cn/alpine/v3.13/main\n\
https://mirror.tuna.tsinghua.edu.cn/alpine/v3.13/community" > /etc/apk/repositories
RUN apk --update add curl bash ttf-dejavu && \
      rm -rf /var/cache/apk/*

CMD java -Xmx128m -Xmx128m -jar -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 app.jar

ENTRYPOINT ["java","-Xmx128m","-Xmx128m","-jar","-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005","/app.jar"]