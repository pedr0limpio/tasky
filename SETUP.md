# Tips and Infos for setup Tasky 

## The project uses:
   
#### - Oracle OpenJDK 23.0.2 9 (for all components)
#### - Initial project of IntelliJ 
1. File > New > Project > Quarkus > Java-Maven (checkbox Java 21 - I don't know why)
2. Quarkus plugins = SmallRye GraphQL and JDBC Driver - MySQL

#### - Mermaid plugin
#### - Qodana plugin for code analysis
#### - JaCoCo code coverage

## Database Information
1. JDBC MySQL driver for Quarkus;
2. DB installed on premise (localhost)
3. If you like, use DBeaver for manipulate DB;
4. The application.properties file stores DB configuration;
#### SQL Code using for create tables
```tsql
CREATE TABLE TAGS (
tag_id INT AUTO_INCREMENT PRIMARY KEY,
name VARCHAR(10) NOT NULL
);

CREATE TABLE TASKS (
task_id INT AUTO_INCREMENT PRIMARY KEY,
description VARCHAR(50) NOT NULL,
priority VARCHAR(10) NOT NULL,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
conclusion_at TIMESTAMP NULL
);

CREATE TABLE TASK_TAGS (
task_tags_id INT AUTO_INCREMENT PRIMARY KEY,
task_id INT,
tag_id INT,
FOREIGN KEY (task_id) REFERENCES TASKS(task_id),
FOREIGN KEY (tag_id) REFERENCES TAGS(tag_id)
);
```
## Miscellaneous
1. Qodana have a cloud for code analysis. Try it;
2. I use graphql-api.http (HTTP Client plugin build-in for IntelliJ) for dev testing of API;
   - Content of graphql-api.http:
```http request
GRAPHQL http://localhost:8080/graphql
    
query {
    sayHello(name: "Pedro")
}

###
POST http://localhost:8080/graphql

{
    "query": "query($name: String!) {
        sayHello(name: $name)
    }",
    "variables": {
        "name": "Alice"
    }
}

###
POST http://localhost:8080/graphql

{
    "query": "{
        sayHello(name: \"Pedro\") {
        name,
        surname
        }
    }"
}

###
GRAPHQL http://localhost:8080/graphql

mutation {
    newTask(task: {
    description: "test project",
    priority: Info,
    tagList: [Personal, Game],
    creation: "2025-02-02T17:02:01Z",
    conclusion: null
}) {
    id
    description
    priority
    tagList
    creation
    conclusion
    }
}

###
```
3. Useful end-points:
   1. [GraphQL Schema API](http://localhost:8080/graphql/schema.graphql) - API documentation 
   2. [GraphQL UI](http://localhost:8080/q/graphql-ui/) - A playground for API request
   3. [Mermaid Documentation](https://mermaid.js.org/intro/) - Mermaid doc site 
### Extra
+ Check directory doc and find TODO over the project. Good vibes!
+ Just out of curiosity: The tests were created using the TDD technique.
+ Try Todosaurus plugin on IntelliJ.
```
Passo a Passo para Usar o Plugin Todosaurus
Instalar o Plugin:
Abra o IntelliJ IDEA.
Vá para Settings/Preferences > Plugins > Marketplace.
Procure por "Todosaurus" e clique em "Install".

Configurar o Plugin:

Vá para Settings/Preferences > Todosaurus.
Certifique-se de que o padrão TODO: está habilitado (ele está habilitado por padrão).
Registre seu token do GitHub em File > Settings > Version Control > GitHub.
Criar uma Issue a Partir de um Comentário TODO:
Abra o TODO tool window no IntelliJ.
Clique com o botão direito no comentário TODO que deseja vincular.
Selecione a opção "Create Issue".
Preencha os detalhes da issue e clique em "Create Issue".

Atualizar o Comentário TODO:
O comentário TODO será atualizado automaticamente com o número da issue, por exemplo, // TODO[#123]: Fix this code
```