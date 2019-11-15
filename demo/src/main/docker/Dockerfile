####
# This Dockerfile is used in order to build a container that runs the Quarkus application in JVM mode
#
# Build the image with:
#
# docker build -f src/main/docker/Dockerfile -t triad/training/jwt/demo .
#
# Then run the container using:
#
# docker run -p 7200:80 triad/training/jwt/demo
#
###
FROM node:10
RUN mkdir /opt/app
COPY src/main/node/* /opt/app/
WORKDIR /opt/app/
CMD ["node", "app.js"]
