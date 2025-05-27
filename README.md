# Documentação do Projeto - Gerenciamento de Veículos

## Descrição

Sistema web para gerenciamento de veículos, permitindo cadastro, edição, visualização e exclusão de carros e motos. O projeto utiliza Java, Spring Boot, JDBC, MySQL, Thymeleaf, jQuery e Bootstrap.

---

## Tecnologias Utilizadas

- **Java 17+**
- **Spring Boot** (Web)
- **MySQL** (Banco de dados relacional)
- **JDBC** (Acesso ao banco de dados)
- **Thymeleaf** (Template engine)
- **jQuery** (Manipulação DOM/AJAX)
- **Bootstrap** (Estilização e componentes)
- **Bootstrap Icons** (Ícones)
- **Maven** (Gerenciamento de dependências)
- **Npm** (Gerenciamento de pacotes front-end)

---

## Como Montar o Ambiente

### Pré-requisitos

- Java 17 ou superior
- Maven 3.8+
- MySQL 8+
- Node.js e npm

### Passos

1. **Clone o repositório**
   ```sh
   git clone https://github.com/thayddo/desafio
   cd desafio
   ```
   
2. **Configure o banco de dados**
- Crie o banco `gerenciamento_veiculos` no MySQL.
- Ajuste usuário e senha em `src/main/resources/application.properties` se necessário.
3. **Instale dependências front-end**
```sh
npm install
```
4. **Copie os ícones do Bootstrap para o projeto**
- **Windows:**
```sh
npm run icons-windows
```
- **Unix/Linux/Mac:**
```sh
npm run icons-unix
```
---
## Como Subir o Ambiente
### Sem Docker

1. Certifique-se que o MySQL está rodando e o banco foi criado.
2. Compile e rode o projeto:
```sh
./mvnw spring-boot:run
```
ou
```sh
mvn spring-boot:run
```
3. Acesse [http://localhost:8080](http://localhost:8080)

Suba o serviço:
```sh
docker-compose up -d --build
```

2. Aguarde o banco iniciar e rode a aplicação localmente (passos acima).

---

## Endpoints Principais
- `GET /` — Lista todos os veículos
- `GET /veiculos/{id}` — Detalha um veículo
- `POST /veiculos` — Cria um novo veículo
- `PUT /veiculos/{id}` — Atualiza um veículo existente
- `DELETE /veiculos/{id}` — Remove um veículo
- 
---
## Observações
- O front-end está em `src/main/resources/static/`
- O back-end utiliza Spring Boot e Thymeleaf.
- Para dúvidas, consulte os comentários no código ou abra uma issue.