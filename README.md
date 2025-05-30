# UTFPR Remote Server Connectivity

Este projeto foi desenvolvido como parte das atividades da especialização em Dispositivos Móveis na UTFPR-PB. Seu objetivo é monitorar a conectividade com servidores remotos, fornecendo relatórios e análises sobre a disponibilidade e desempenho das conexões comparando o desempenho do Ktor-client com HttpURLConnection.

## 📂 Estrutura do Projeto

- **QuoteAPI**: Serviço backend desenvolvido em **Java com Spring Boot**, utilizando **Maven**. Expõe uma API REST para consulta de frases.
- **QuoteClient**: Aplicação cliente desenvolvida em **Kotlin**, utilizando **Gradle**, que consome a API exposta, realiza chamadas periódicas e gera relatórios com os dados obtidos.

## 🛠️ Tecnologias Utilizadas

- **Java** & **Kotlin**
- **Spring Boot** (para o módulo QuoteAPI)
- **Maven** (para build do QuoteAPI)
- **Gradle** (para build do QuoteClient)
- **Docker** (para o módulo QuoteAPI)
- **HTTP Client**

## 🚀 Como Executar

### ✅ Pré-requisitos

- Java 11 ou superior
- Docker instalado e em execução
- Maven e Gradle instalados (ou utilizar os wrappers: `mvnw` / `gradlew`)

---

### ▶️ Executando a API (QuoteAPI)

#### 🔧 Opção 1: Executando com Maven (modo local)

1. Acesse o diretório do módulo:

   ```bash
   cd QuoteAPI
   ```

2. Construa o projeto com Maven:

   ```bash
   ./mvnw clean install
   ```

3. Execute a aplicação localmente:

   ```bash
   ./mvnw spring-boot:run
   ```

> A API estará disponível por padrão em `http://localhost:8085`.

---

#### 🐳 Opção 2: Executando com Docker

1. Certifique-se de que o Docker está em execução.

2. Acesse o diretório `QuoteAPI`:

   ```bash
   cd QuoteAPI
   ```

3. Construa a imagem Docker:

   ```bash
   docker build -t quoteapi .
   ```

4. Execute o container:

   ```bash
   docker run -p 8085:8085 quoteapi
   ```

> Isso disponibilizará a API via Docker em `http://localhost:8085`.

---

### ▶️ Executando o Cliente (QuoteClient)

1. Em outro terminal, vá para o diretório:

   ```bash
   cd QuoteClient
   ```

2. Construa o projeto com Gradle:

   ```bash
   ./gradlew build
   ```

3. Execute a aplicação cliente:

   ```bash
   ./gradlew run
   ```

> O cliente fará chamadas à API `QuoteAPI`, processará as respostas e gerará relatórios de conectividade.
