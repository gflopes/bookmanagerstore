<h2>Bookstore API Manager</h2>

O objetivo do projeto Bookstore API Manager é disponibilizar uma API para cadastro dos livros de uma livraria através de uma API REST.

Durante o projeto, são abordados os seguintes tópicos:

* Setup inicial de projeto com o Spring Boot Initialzr.
* Criação de modelo de dados para o mapeamento de entidades em bancos de dados.
* Configuração do nosso projeto no SonarCloud, e como a ferramenta dá apoio no aumento da qualidade do nosso projeto.
* Configuraçao do TravisCI como ferramenta de integração contínua.
* Desenvolvimento de operações de gerenciamento de usuários, livros, autores e editora.
* Desenvolvimento de autenticaçao e autorização de usuários através do Spring Security, e com suporte a JWT.
* Relação de cada uma das operações acima com o padrão arquitetural REST, e a explicação de cada um dos conceitos REST envolvidos durante o desenvolvimento do projeto.
* Desenvolvimento de testes unitários para validação das funcionalidades.
* Apresentação do TDD (Test Driven Development), e como desenvolver funcionalidade na prática com o TDD.
* Desenvolvimento de testes de integração com o Postman.
* Implantação do sistema na nuvem através do Heroku.

Para executar o projeto no terminal, digite o seguinte comando:

```shell script
mvn spring-boot:run 
```

Após executar o comando acima, basta apenas abrir o seguinte endereço e visualizar a execução do projeto:

```
http://localhost:8090/api/v1/books
```

Para abrir a documentação (disponibilizada através do Swagger 2) de todas as operações implementadas com o padrão arquitetural REST, acesse o seguinte link abaixo:

```
http://localhost:8090/swagger-ui/index.html
```

Após executar o comando acima, basta apenas abrir o seguinte endereço e visualizar a execução do projeto:

As seguintes ferramentas abaixo são utilizadas como part do desenvolvimento do projeto prático do curso:

* Java 11 ou versões superiores.
* Banco de dados H2 como SGBD do nosso projeto (em ambos ambientes, Dev e Prod)
* SonarCloud para verificaçao da qualidade de código do nosso projeto.
* TravisCI como ferramenta de integração contínua.
* Open API para a documentação de todos os endpoints desenvolvidos dentro do projeto.
* Conta no Heroku para o deploy do projeto na nuvem