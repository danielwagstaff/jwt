####
# This Dockerfile is used in order to build a container that runs the Quarkus application in JVM mode
#
# Before building the docker image run:
#
# mvn package
#
# Then, build the image with:
#
# docker build -f src/main/docker/Dockerfile -t triad/training/jwt/greeting .
#
# Then run the container using:
#
# docker run -p 7100:80 triad/training/jwt/greeting
#
###
FROM openjdk:13
RUN mkdir /opt/app
COPY target/*-runner.jar /opt/app/app.jar
CMD ["java", "-jar", "/opt/app/app.jar"]
