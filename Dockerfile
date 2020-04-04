# 1st stage, build the app and a JRE optimized for running it
FROM alpine:latest AS builder
RUN apk update                                     \
 && apk --no-cache add openjdk11-jdk openjdk11-jmods
# add newly installed jlink and jdeps to PATH
ENV PATH /usr/lib/jvm/java-11-openjdk/bin:$PATH
WORKDIR /build
# init gradle version defined by build and load dependencies
ADD gradle gradle
ADD gradle.* *.gradle* gradlew ./
RUN ./gradlew clean --no-daemon --refresh-dependencies
# build app
ADD src src
ADD build-jar ./
RUN ./build-jar
# create in directory ./optimized-jre a JRE optimized for running this app
ADD create-optimized-jre ./
RUN ./create-optimized-jre

# 2nd stage, build the runtime image
FROM alpine:latest
RUN apk update                       \
 && apk --no-cache add ca-certificates
WORKDIR /app
# copy the optimized JRE and app built in the 1st stage
COPY --from=builder /build/optimized-jre      jre
COPY --from=builder /build/build/*-runner.jar app.jar
COPY --from=builder /build/build/lib          lib
EXPOSE 8080
ENTRYPOINT ["./jre/bin/java", "-jar", "app.jar"]
