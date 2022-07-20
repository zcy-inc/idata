FROM adoptopenjdk/openjdk11:x86_64-centos-jre-11.0.10_9
RUN mkdir /app
ADD idata-portal-1.0.0.jar /app/app.jar
ADD startup.sh /app/
WORKDIR /app
RUN chmod a+x startup.sh
CMD /app/startup.sh