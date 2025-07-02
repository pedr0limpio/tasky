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
./mvnw quarkus:dev # if on linux
.\mvnw.cmd quarkus:dev # if on windows powershell
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

## Using the GraphQL API with Insomnia

You can test the Tasky GraphQL API using [Insomnia](https://insomnia.rest/):

1. **Find the GraphQL Endpoint**

   The GraphQL endpoint is usually available at:
   ```
   http://localhost:8080/graphql
   ```

2. **Import the Predefined Insomnia Workspace**

   - In this repository, you will find a pre-configured Insomnia workspace file at:
     ```
     doc/insomnia/insomnia.json
     ```
   - In Insomnia, go to **Application** menu > **Import/Export** > **Import Data** > **From File**.
   - Select the `doc/insomnia/insomnia.json` file.
   - This will import all relevant requests for Tasky.

3. **Manual Setup (Optional)**

   - Alternatively, you can create a new request manually:
     - Click on the **Create** button and select **Request**.
     - Choose **GraphQL Query** as the request type.
     - Set the request URL to `http://localhost:8080/graphql`.
     - Run the app in dev mode using:
       ```shell
       ./mvnw quarkus:dev # if on linux
       .\mvnw.cmd quarkus:dev # if on windows powershell
       ```
     - Paste your GraphQL query (for example, to list all tasks):

       ```graphql
       query {
         listAll {
           taskId
           title
           description
           tags {
             name
           }
         }
       }
       ```

     - Click **Send** to execute the query and see the results.

4. **Tip:**  
   You can save your requests in a collection for easy access later.