FROM docker-image.cai-inc.com/compile/mvn:3.5-jdk11 as builder
ENV JAVA_HOME=/opt/jdk-11.0.8
COPY . /app
WORKDIR /app
RUN mvn clean package

FROM docker-image.cai-inc.com/compile/openjdk11:x86_64-centos-jre-11.0.10_9
RUN mkdir /app
COPY --from=builder /app/idata-portal/target/idata-portal-1.0.0.jar /app/app.jar
ADD idata-portal/docker/startup.sh /app/
WORKDIR /app
RUN chmod a+x startup.sh
CMD /app/startup.sh