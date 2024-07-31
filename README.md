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

## Design Patterns
- Repository Pattern
- Service Layer Pattern
## Solid
- Single Responsibility Principle (SRP)
 PixKey Class: Deve representar apenas os dados da chave Pix. A validação e a lógica de negócios devem estar em classes separadas.
 PixKeyService: Deve conter apenas a lógica de negócios relacionada à chave Pix. Qualquer lógica de validação deve estar em classes ou serviços separados.
 PixKeyValidator: Cada validador (e-mail, CPF, CNPJ, etc.) deve ter sua própria classe responsável apenas pela validação do tipo específico de chave.
## 12 factor-apps
- Código Base (Codebase):
Prática: O princípio de uma única codebase para um único repositório. Isso facilita o versionamento e o gerenciamento do código.
- Dependências (Dependencies):
Prática: A aplicação declara suas dependências explicitamente (por exemplo, no build.gradle para Java). O uso de ferramentas como Gradle para gerenciar dependências é uma boa prática conforme o 12-Factor.
- Configuração (Configuration):
Prática: As configurações da aplicação são extraídas do código-fonte e mantidas em variáveis de ambiente, conforme indicado pelo uso do Spring Boot com profiles e propriedades externas.
- Backends de Serviços (Backing Services):
Prática: A aplicação se conecta a serviços externos, como MongoDB, para persistência de dados, o que está alinhado com a prática de tratar serviços externos como recursos plugáveis.
- Build, Release, Run (Build, Release, Run):
Prática: A aplicação segue o ciclo de build (compilação), release (implantação) e run (execução), conforme o fluxo típico de aplicações Java e o uso de ferramentas como Gradle para construção e execução.
- Logs (Logs):
Prática: O uso de frameworks como SLF4J e Logback para logging é compatível com o tratamento de logs como streams de eventos.
- Port Binding (Port Binding):
Prática: A aplicação deve escutar em uma porta definida através de variáveis de ambiente via application.yml.

## Considerações

Para contribuir com o projeto, por favor, siga as diretrizes de contribuição e crie um pull request. Para mais detalhes, consulte a [documentação](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/) do Spring Boot.
