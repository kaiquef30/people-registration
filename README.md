# Sistema de Gerenciamento de Pessoas e Endereços

Este é um sistema de gerenciamento de pessoas e endereços que permite criar, atualizar, visualizar e excluir informações de pessoas e seus endereços.

![Classe UML (1)](https://github.com/kaiquef30/people-registration/assets/109758907/8f017679-e15a-4929-87ad-e49149c656b4)

## Funcionalidades Principais

- Gerenciamento de Pessoas:
  - Criação, atualização, visualização e exclusão de pessoas.
  - Associação de endereços a pessoas.

- Gerenciamento de Endereços:
  - Criação, atualização, visualização e exclusão de endereços.
  - Definição de endereço principal para uma pessoa.

## Arquitetura e Tecnologias Utilizadas

- Arquitetura baseada em camadas (Controller, Service, Repository).
- Tecnologias:
  - Java
  - Spring Boot
  - Spring Data JPA
  - ModelMapper
  - PostgreSQL
  - JUnit e Mockito (para testes unitários)

## Funcionalidades Adicionais

- Suporta consumo e produção de dados no formato XML.
- Utiliza Aspect-Oriented Programming (AOP) para limitar o número de requisições.
- Código limpo e seguindo os princípios SOLID (Single Responsibility, Open/Closed, Liskov Substitution, Interface Segregation, Dependency Inversion).

## Como Executar o Projeto

1. Clone o repositório para sua máquina local.
2. Importe o projeto em sua IDE Java.
3. Certifique-se de ter as dependências do Maven instaladas.
4. Configure o arquivo `application.properties` com as configurações necessárias do banco de dados.
5. Execute a classe principal `Application.java`.

## Testes Unitários

- O projeto inclui testes unitários para as classes de serviço (UseCase) utilizando JUnit e Mockito.
- Para executar os testes, utilize sua IDE ou execute `mvn test` no terminal na raiz do projeto.

## Autor

Este projeto foi desenvolvido por Kaique Fernando como parte de um projeto de gerenciamento de informações.

Qualquer dúvida ou sugestão, entre em contato pelo e-mail kaiquefernandosantana@gmail.com



Link de acesso para documentação da API: https://people-registration.onrender.com/swagger-ui/index.html
