# Based on https://hub.docker.com/r/adoptopenjdk/openjdk14
FROM registry.access.redhat.com/ubi8/openjdk-11:1.3

RUN mkdir /src && chmod a+rwx /src
COPY . /src
WORKDIR /src
RUN whoami

RUN ./gradlew --no-daemon shadowJar \
  && cp /src/build/libs/kowsay.jar /bin/kowsay.jar \
  && rm -rf /src

WORKDIR /bin

ENTRYPOINT ["java","-jar","kowsay.jar"]

