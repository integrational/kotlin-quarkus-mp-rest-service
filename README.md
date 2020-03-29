# kotlin-quarkus-mp-rest-service project

## How this app was generated

```
docker run --rm --name mvn -it -v $(pwd):/proj -w /proj maven:3-jdk-11 /bin/bash -c "\
	apt-get update; \
	apt-get install -y --no-install-recommends gradle; \
	mvn io.quarkus:quarkus-maven-plugin:1.3.1.Final:create \
		-DprojectGroupId=org.integrational \
		-DprojectArtifactId=kotlin-quarkus-mp-rest-service \
		-DclassName='org.integrational.greetings.restapi.GreetingsResource' \
		-Dpath='/greetings' \
		-Dextensions='kotlin,smallrye-fault-tolerance,smallrye-health,smallrye-jwt,smallrye-metrics,smallrye-openapi,smallrye-opentracing,rest-client,arc,resteasy,resteasy-jsonb,jsonb,jsonp' \
		-DbuildTool=gradle"
```

This fails in the final Gradle build, which generates the Gradle wrapper. Hence: 

```
cd kotlin-quarkus-mp-rest-service
docker run --rm --name gradle -it -v $(pwd):/proj -w /proj gradle:jdk11 gradle wrapper
```

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```
./gradlew quarkusDev
```

## Packaging and running the application

The application can be packaged using `./gradlew quarkusBuild`.
It produces the `kotlin-quarkus-mp-rest-service-1.0-SNAPSHOT-runner.jar` file in the `build` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `build/lib` directory.

The application is now runnable using `java -jar build/kotlin-quarkus-mp-rest-service-1.0-SNAPSHOT-runner.jar`.

If you want to build an _über-jar_, just add the `--uber-jar` option to the command line:
```
./gradlew quarkusBuild --uber-jar
```

## Creating a native executable

You can create a native executable using: `./gradlew buildNative`.

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: `./gradlew buildNative --docker-build=true`.

You can then execute your native executable with: `./build/kotlin-quarkus-mp-rest-service-1.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/gradle-tooling#building-a-native-executable.