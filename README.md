# Projeto bank-backend

##### Configuração Inicial

- IDE: Eclipse
- Java: JDK15
- Lombok
- Instalar o Spring Tool Suite a partir do Eclipse Marktplace
- Maven

##### Nova "Launch configuration"
Criar uma nova "Launch configuration" para inicializar o servidor em modo Run ou Debug acessando: **Menu - Run > Debug configurations... > Spring Boot App > New launch configuration > Spring Boot**, informando os seguintes detalhes:

```
Name: bank-backend
Project: bank-backend
Main type: br.com.donus.bankbackend.BankBackendApplication
```

Tudo deve estar pronto para executar o sistema acessando **Run>Run Configurations...>Spring Boog App>bank-backend "Run"**.

Após configurar o ambiente e iniciar a aplicação, estará disponível no endereço: 

```
http://localhost:8080/bank-backend
```
##### Swagger

```
http://localhost:8080/bank-backend/swagger-ui.html#/
```

##### Autenticação
```
usuário default: user
senha: pegar do log da aplicação

Exemplo log: Using generated security password: 43d82464-f790-4e80-a67a-924024b9d044
```

##### Base de Dados
```
Base H2 - Memória
```
