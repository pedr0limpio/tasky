![build badge](https://github.com/pedr0limpio/tasky/actions/workflows/maven.yml/badge.svg?event=push&branch=main)
# Tasky - a simple todo tool

This project is for educational purposes only and its code is intentionally incomplete. Just to complete and learn.
Also uses Quarkus, the Supersonic Subatomic Java Framework.
If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

### Heads up!
Make sure you have JAVA_HOME and MAVEN_HOME properly set on both OS and IDE.
You may want to ask any GPT how to set those.
Also, make sure you test clean and install your Maven installation.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw quarkus:dev
```
> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

To build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```
The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```
You can then execute your native executable with: `./target/tasky-1.0-SNAPSHOT-runner`
If you want to learn more about building native executables, please consult <https://quarkus.io/guides/maven-tooling>.

## Related Guides

- SmallRye GraphQL ([guide](https://quarkus.io/guides/smallrye-graphql)): Create GraphQL Endpoints using the code-first
  approach from MicroProfile GraphQL.
- Tips and Infos ([here](SETUP.md))