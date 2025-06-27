# Exemplos de Teste do MCP Server

Este arquivo contém exemplos de como testar o MCP Server implementado em Java.

## 🚀 Iniciando o Servidor

Primeiro, compile e execute o servidor:

```bash
cd java-mcp
mvn clean compile
mvn exec:java
```

O servidor estará disponível via STDIO e pode ser testado com o MCP Inspector.

## 🔍 Testando com MCP Inspector

### 1. Instale o MCP Inspector

```bash
npm install -g @modelcontextprotocol/inspector
```

### 2. Compile o projeto

```bash
cd java-mcp
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

## 🧪 Testando as Ferramentas

### 1. **calculator**
Teste a calculadora com diferentes expressões:

```json
{
  "expression": "2 + 3"
}
```

**Resposta esperada:**
```json
{
  "expression": "2 + 3",
  "result": 5.0,
  "message": "Cálculo realizado com sucesso"
}
```

**Mais exemplos:**
```json
{"expression": "10 - 4"}  // Resultado: 6.0
{"expression": "6 * 7"}   // Resultado: 42.0
{"expression": "15 / 3"}  // Resultado: 5.0
```

### 2. **greeter**
Teste o saudador com diferentes nomes:

```json
{
  "name": "Maria"
}
```

**Resposta esperada:**
```json
{
  "message": "Olá, Maria! Bem-vindo ao Java MCP Server!",
  "timestamp": 1703123456789,
  "greeted_name": "Maria"
}
```

**Sem nome (usa padrão):**
```json
{}
```

### 3. **get_info**
Obtenha informações sobre o servidor:

```json
{}
```

**Resposta esperada:**
```json
{
  "name": "Java MCP Server Example",
  "version": "1.0.0",
  "description": "Exemplo simples de MCP Server usando Java SDK",
  "java_version": "24.0.1",
  "timestamp": 1703123456789,
  "available_tools": ["calculator", "greeter", "get_info"]
}
```

## 🐍 Testando com Python

Crie um arquivo `test.py`:

```python
import requests
import json
import subprocess
import sys

def test_mcp_server():
    # Iniciar o servidor MCP
    process = subprocess.Popen(
        ["java", "-jar", "target/java-mcp-1.0-SNAPSHOT.jar"],
        stdin=subprocess.PIPE,
        stdout=subprocess.PIPE,
        stderr=subprocess.PIPE,
        text=True
    )
    
    try:
        # Teste 1: Handshake inicial
        print("1. Testando handshake inicial...")
        handshake = {
            "jsonrpc": "2.0",
            "method": "initialize",
            "id": 1,
            "params": {
                "protocolVersion": "2024-11-05",
                "capabilities": {"tools": True, "logging": True},
                "clientInfo": {"name": "test-client", "version": "1.0.0"}
            }
        }
        
        process.stdin.write(json.dumps(handshake) + "\n")
        process.stdin.flush()
        
        response = process.stdout.readline()
        print("Resposta:", response.strip())
        
        # Teste 2: Listar ferramentas
        print("\n2. Listando ferramentas...")
        tools_request = {
            "jsonrpc": "2.0",
            "method": "tools/list",
            "id": 2
        }
        
        process.stdin.write(json.dumps(tools_request) + "\n")
        process.stdin.flush()
        
        response = process.stdout.readline()
        print("Resposta:", response.strip())
        
        # Teste 3: Usar calculadora
        print("\n3. Testando calculadora...")
        calc_request = {
            "jsonrpc": "2.0",
            "method": "tools/call",
            "id": 3,
            "params": {
                "name": "calculator",
                "arguments": {"expression": "10 * 5"}
            }
        }
        
        process.stdin.write(json.dumps(calc_request) + "\n")
        process.stdin.flush()
        
        response = process.stdout.readline()
        print("Resposta:", response.strip())
        
        # Teste 4: Usar saudador
        print("\n4. Testando saudador...")
        greet_request = {
            "jsonrpc": "2.0",
            "method": "tools/call",
            "id": 4,
            "params": {
                "name": "greeter",
                "arguments": {"name": "Ana"}
            }
        }
        
        process.stdin.write(json.dumps(greet_request) + "\n")
        process.stdin.flush()
        
        response = process.stdout.readline()
        print("Resposta:", response.strip())
        
    except Exception as e:
        print(f"Erro nos testes: {e}")
    finally:
        process.terminate()
        process.wait()

if __name__ == "__main__":
    test_mcp_server()
```

Execute com:
```bash
cd java-mcp
python test.py
```

## 🔧 Testando Erros

### Requisição inválida para calculadora:
```json
{
  "jsonrpc": "2.0",
  "method": "tools/call",
  "id": 5,
  "params": {
    "name": "calculator",
    "arguments": {"expression": "invalid"}
  }
}
```

### Método não suportado:
```json
{
  "jsonrpc": "2.0",
  "method": "unsupported_method",
  "id": 6
}
```

### Ferramenta não encontrada:
```json
{
  "jsonrpc": "2.0",
  "method": "tools/call",
  "id": 7,
  "params": {
    "name": "inexistent_tool",
    "arguments": {}
  }
}
```

## 📊 Monitoramento

Para monitorar o servidor em tempo real, você pode usar:

```bash
# Verificar se o servidor está rodando
ps aux | grep java-mcp

# Monitorar logs (se implementado)
tail -f /var/log/mcp-server.log

# Teste de carga simples
for i in {1..10}; do
  echo '{"jsonrpc":"2.0","method":"tools/call","id":'$i',"params":{"name":"get_info","arguments":{}}}' | java -jar target/java-mcp-1.0-SNAPSHOT.jar
  echo "Request $i completed"
done
```

## 🎯 Próximos Passos

1. Implemente autenticação
2. Adicione mais ferramentas (file operations, database queries, etc.)
3. Implemente recursos reais (file system, APIs externas)
4. Adicione logging estruturado
5. Implemente rate limiting
6. Adicione testes automatizados
7. Configure CI/CD 