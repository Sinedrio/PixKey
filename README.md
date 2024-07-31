# Pix Key Manager

## Descrição

O **Pix Key Manager** é uma aplicação para gerenciar chaves Pix. A aplicação permite criar, atualizar, consultar e deletar chaves Pix, integrando-se com um banco de dados MongoDB. As operações suportadas são:

- **Criação** de novas chaves Pix
- **Atualização** de chaves Pix existentes
- **Deleção** de chaves Pix
- **Consulta** de chaves Pix por ID, tipo de chave, conta e agência

## Funcionalidades

- **Criação de Chaves Pix**: Adiciona novas chaves Pix ao sistema com informações como tipo de chave, valor da chave, tipo de conta, e mais.
- **Atualização de Chaves Pix**: Permite alterar o valor e outras informações de chaves Pix existentes.
- **Deleção de Chaves Pix**: Inativa chaves Pix registradas, impedindo alterações e consultas futuras.
- **Consulta de Chaves Pix**: Permite buscar chaves Pix por ID, tipo de chave, número da conta e número da agência.

## Requisitos

- Java 17
- Spring Boot
- MongoDB
- Docker (opcional, para containerização)

## Instalação

### Clonando o Repositório

```bash
git clone https://github.com/seu-usuario/pix-key-manager.git
cd pix-key-manager
```

### Configuração do Ambiente

Certifique-se de ter o Java 17 e o MongoDB instalados. Para rodar o MongoDB, você pode usar o Docker:

```bash
docker run -d -p 27017:27017 --name mongodb mongo:4.4
```

### Dependências

Instale as dependências do projeto com o Gradle:

```bash
./gradlew build
```

### Configuração do Banco de Dados

Certifique-se de que o MongoDB está rodando na porta padrão (27017) ou ajuste as configurações no arquivo `application.properties` para apontar para o seu banco de dados.

### Rodando a Aplicação

Para iniciar a aplicação, use o comando:

```bash
./gradlew bootRun
```

## Testes

### Testes Unitários

Os testes unitários podem ser executados com:

```bash
./gradlew test
```

### Testes Integrados

Para executar testes integrados, use o comando:

```bash
./gradlew integrationTest
```

## Endpoints da API

### Criação de Chave Pix

**POST** `/api/pix-keys`

**Body**:

```json
{
    "keyType": "email",
    "keyValue": "test@example.com",
    "accountType": "corrente",
    "agencyNumber": "1234",
    "accountNumber": "12345678",
    "firstName": "John",
    "lastName": "Doe"
}
```

### Atualização de Chave Pix

**PUT** `/api/pix-keys/{id}`

**Body**:

```json
{
    "keyType": "email",
    "keyValue": "updated@example.com",
    "accountType": "corrente",
    "agencyNumber": "1234",
    "accountNumber": "12345678",
    "firstName": "John",
    "lastName": "Doe"
}
```

### Deleção de Chave Pix

**DELETE** `/api/pix-keys/{id}`

### Consulta de Chave Pix por ID

**GET** `/api/pix-keys/{id}`

### Consulta de Chave Pix por Filtros

**GET** `/api/pix-keys`

**Params**:

- `keyType` (opcional)
- `agencyNumber` (opcional)
- `accountNumber` (opcional)
- `firstName` (opcional)
- `lastName` (opcional)

## Design e Padrões

### SOLID

- **Single Responsibility Principle (SRP)**: Cada classe tem uma única responsabilidade.
- **Open/Closed Principle (OCP)**: As classes são abertas para extensão e fechadas para modificação.
- **Liskov Substitution Principle (LSP)**: As subclasses podem substituir as superclasses sem alterar a funcionalidade esperada.
- **Interface Segregation Principle (ISP)**: As interfaces são específicas para o cliente.
- **Dependency Inversion Principle (DIP)**: As classes dependem de abstrações e não de implementações concretas.

### Design Patterns

- **Factory Method**: Usado para criar instâncias de validação de chaves Pix.
- **Strategy Pattern**: Aplicado para escolher o algoritmo de validação de chave Pix com base no tipo de chave.

## Considerações

Para contribuir com o projeto, por favor, siga as diretrizes de contribuição e crie um pull request. Para mais detalhes, consulte a [documentação](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/) do Spring Boot.

---

Esse README cobre as principais seções para documentar seu projeto e orientar outros desenvolvedores a utilizar e contribuir com sua aplicação. Se houver mais informações específicas sobre sua aplicação, sinta-se à vontade para adicionar mais detalhes.
