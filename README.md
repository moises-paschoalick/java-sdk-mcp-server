# Java SDK MCP Server

Este projeto demonstra como criar um **Model Context Protocol (MCP) Server** usando o Java SDK oficial. O MCP é um protocolo que permite que ferramentas de IA conversem com sistemas externos de forma padronizada.

## 📋 Pré-requisitos

- **Java 24** ou superior
- **Maven** 3.6+ 
- **Git**

## 🚀 Como Executar o Projeto

### 1. Clone o repositório

```bash
git clone <url-do-repositorio>
cd java-sdk-mcp-server
```

### 2. Navegue para o diretório do projeto MCP

```bash
cd java-mcp/java-mcp
```

### 3. Compile o projeto

```bash
mvn clean compile
```

### 4. Execute o projeto

```bash
mvn exec:java
```

Ou crie um JAR executável e execute:

```bash
mvn clean package
java -jar target/java-mcp-1.0-SNAPSHOT.jar
```

## 🏗️ Estrutura do Projeto

```
java-sdk-mcp-server/
├── java-mcp/
│   └── java-mcp/
│       ├── src/
│       │   ├── main/
│       │   │   ├── java/
│       │   │   │   └── org/example/
│       │   │   │       └── Main.java
│       │   │   └── resources/
│       │   └── test/
│       ├── pom.xml
│       └── .gitignore
└── mpc-inspector/
```

## 🔧 Funcionalidades Implementadas

O servidor MCP implementa as seguintes ferramentas:

### 1. **calculator**
Calculadora simples que avalia expressões matemáticas básicas.

**Parâmetros:**
- `expression`: Expressão matemática (ex: "2 + 3", "10 * 5")

**Exemplo de uso:**
```json
{
  "expression": "15 + 25"
}
```

### 2. **greeter**
Saudação personalizada.

**Parâmetros:**
- `name`: Nome da pessoa para saudar

**Exemplo de uso:**
```json
{
  "name": "João"
}
```

### 3. **get_info**
Obtém informações sobre o servidor MCP.

**Parâmetros:** Nenhum

## 🧪 Testando com MCP Inspector

Para testar o servidor usando o MCP Inspector:

### 1. Instale o MCP Inspector

```bash
npm install -g @modelcontextprotocol/inspector
```

### 2. Execute o servidor

```bash
cd java-mcp/java-mcp
mvn clean package
```

### 3. Execute o MCP Inspector

```bash
# Obtenha o caminho completo do JAR
FULL_PATH=$(pwd)/target/java-mcp-1.0-SNAPSHOT.jar
echo $FULL_PATH

# Execute o inspector
npx @modelcontextprotocol/inspector java -jar $FULL_PATH
```

### 4. No Inspector:
- Verifique a conexão do servidor no painel de conexão
- Navegue para a aba "Tools" para ver as ferramentas disponíveis
- Teste cada ferramenta clicando nelas e visualizando a resposta
- Monitore os logs no painel de Notificações

## 🔌 Integrando com Claude Desktop

Para usar este MCP server com Claude Desktop:

### 1. Obtenha o caminho completo do JAR

```bash
cd java-mcp/java-mcp
FULL_PATH=$(pwd)/target/java-mcp-1.0-SNAPSHOT.jar
echo $FULL_PATH
```

### 2. Configure o Claude Desktop

1. Abra as preferências do Claude Desktop
2. Navegue para a seção "MCP Servers"
3. Adicione um novo servidor com a seguinte configuração:

```json
{
  "java-mcp-example": {
    "command": "java",
    "args": [
      "-jar",
      "$FULL_PATH"
    ]
  }
}
```

## 📚 Dependências Principais

- **MCP Java SDK**: `io.modelcontextprotocol.sdk:mcp:0.9.0`
- **Jackson**: Para processamento JSON
- **SLF4J**: Para logging

## 🔍 Exemplo de Implementação

O servidor é implementado usando o padrão do MCP SDK:

```java
// Configurar o transport provider (STDIO)
var transportProvider = new StdioTransportProvider();

// Criar o servidor MCP
var syncServer = McpServer.sync(transportProvider)
    .serverInfo("java-mcp-example", "1.0.0")
    .capabilities(McpSchema.ServerCapabilities.builder()
        .tools(true)
        .logging()
        .build())
    .tools(createTools())
    .build();

// Iniciar o servidor
syncServer.run();
```

## 🎯 Próximos Passos

1. **Adicionar mais ferramentas**: Implemente ferramentas para operações de arquivo, consultas de banco de dados, etc.
2. **Implementar recursos**: Adicione suporte para recursos (Resources) do MCP
3. **Adicionar autenticação**: Implemente mecanismos de segurança
4. **Melhorar logging**: Adicione logging estruturado e métricas
5. **Testes automatizados**: Implemente testes unitários e de integração
6. **CI/CD**: Configure pipeline de build e deploy

## 📖 Recursos Adicionais

- [Documentação oficial do MCP](https://modelcontextprotocol.io/)
- [Especificação do protocolo](https://spec.modelcontextprotocol.io/)
- [Exemplo do Dan Vega](https://github.com/danvega/javaone-mcp)
- [MCP Inspector](https://github.com/modelcontextprotocol/mcp-inspector)

## 🤝 Contribuindo

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo `LICENSE` para mais detalhes.

## 🆘 Suporte

Se você encontrar algum problema ou tiver dúvidas, por favor abra uma issue no repositório.
