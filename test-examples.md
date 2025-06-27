# Exemplos de Teste do MCP Server

Este arquivo contém exemplos de como testar o MCP Server implementado em Java.

## 🚀 Iniciando o Servidor

Primeiro, compile e execute o servidor:

```bash
cd java-mcp/java-mcp
mvn clean compile
mvn exec:java
```

O servidor estará disponível em `http://localhost:3000`

## 🔍 Testando os Endpoints

### 1. Informações do Servidor (GET /)

```bash
curl -X GET http://localhost:3000/
```

**Resposta esperada:**
```json
{
  "name": "Java MCP Server",
  "version": "1.0.0",
  "description": "Exemplo de MCP Server implementado com Java SDK",
  "endpoints": {
    "tools": "/tools",
    "resources": "/resources",
    "health": "/health"
  }
}
```

### 2. Listar Ferramentas Disponíveis (GET /tools)

```bash
curl -X GET http://localhost:3000/tools
```

**Resposta esperada:**
```json
{
  "calculator": {
    "name": "calculator",
    "description": "Calculadora simples",
    "usage": "POST /tools/calculator com JSON: {\"expression\": \"2 + 2\"}"
  },
  "greeter": {
    "name": "greeter",
    "description": "Saudação personalizada",
    "usage": "POST /tools/greeter com JSON: {\"name\": \"João\"}"
  }
}
```

### 3. Usar a Calculadora (POST /tools/calculator)

```bash
curl -X POST http://localhost:3000/tools/calculator \
  -H "Content-Type: application/json" \
  -d '{"expression": "2 + 3"}'
```

**Resposta esperada:**
```json
{
  "expression": "2 + 3",
  "result": 5.0
}
```

**Mais exemplos de cálculos:**
```bash
# Subtração
curl -X POST http://localhost:3000/tools/calculator \
  -H "Content-Type: application/json" \
  -d '{"expression": "10 - 4"}'

# Multiplicação
curl -X POST http://localhost:3000/tools/calculator \
  -H "Content-Type: application/json" \
  -d '{"expression": "6 * 7"}'

# Divisão
curl -X POST http://localhost:3000/tools/calculator \
  -H "Content-Type: application/json" \
  -d '{"expression": "15 / 3"}'
```

### 4. Usar o Saudador (POST /tools/greeter)

```bash
curl -X POST http://localhost:3000/tools/greeter \
  -H "Content-Type: application/json" \
  -d '{"name": "Maria"}'
```

**Resposta esperada:**
```json
{
  "message": "Olá, Maria! Bem-vindo ao MCP Server!",
  "timestamp": 1703123456789
}
```

**Sem nome (usa padrão):**
```bash
curl -X POST http://localhost:3000/tools/greeter \
  -H "Content-Type: application/json" \
  -d '{}'
```

### 5. Listar Recursos (GET /resources)

```bash
curl -X GET http://localhost:3000/resources
```

**Resposta esperada:**
```json
{
  "files": {
    "readme": "/resources/readme",
    "config": "/resources/config",
    "status": "/resources/status"
  }
}
```

### 6. Health Check (GET /health)

```bash
curl -X GET http://localhost:3000/health
```

**Resposta esperada:**
```json
{
  "status": "healthy",
  "timestamp": 1703123456789,
  "uptime": 1703123456789,
  "version": "1.0.0"
}
```

## 🧪 Testando com JavaScript (Node.js)

Crie um arquivo `test.js`:

```javascript
const fetch = require('node-fetch');

async function testMCPServer() {
    const baseUrl = 'http://localhost:3000';
    
    try {
        // Teste 1: Informações do servidor
        console.log('1. Testando informações do servidor...');
        const info = await fetch(baseUrl).then(r => r.json());
        console.log(info);
        
        // Teste 2: Listar ferramentas
        console.log('\n2. Listando ferramentas...');
        const tools = await fetch(`${baseUrl}/tools`).then(r => r.json());
        console.log(tools);
        
        // Teste 3: Usar calculadora
        console.log('\n3. Testando calculadora...');
        const calcResult = await fetch(`${baseUrl}/tools/calculator`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ expression: '5 + 3' })
        }).then(r => r.json());
        console.log(calcResult);
        
        // Teste 4: Usar saudador
        console.log('\n4. Testando saudador...');
        const greetResult = await fetch(`${baseUrl}/tools/greeter`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ name: 'João' })
        }).then(r => r.json());
        console.log(greetResult);
        
        // Teste 5: Health check
        console.log('\n5. Testando health check...');
        const health = await fetch(`${baseUrl}/health`).then(r => r.json());
        console.log(health);
        
    } catch (error) {
        console.error('Erro nos testes:', error);
    }
}

testMCPServer();
```

Execute com:
```bash
node test.js
```

## 🐍 Testando com Python

Crie um arquivo `test.py`:

```python
import requests
import json

def test_mcp_server():
    base_url = 'http://localhost:3000'
    
    try:
        # Teste 1: Informações do servidor
        print("1. Testando informações do servidor...")
        response = requests.get(base_url)
        print(json.dumps(response.json(), indent=2))
        
        # Teste 2: Listar ferramentas
        print("\n2. Listando ferramentas...")
        response = requests.get(f"{base_url}/tools")
        print(json.dumps(response.json(), indent=2))
        
        # Teste 3: Usar calculadora
        print("\n3. Testando calculadora...")
        response = requests.post(
            f"{base_url}/tools/calculator",
            json={"expression": "10 * 5"}
        )
        print(json.dumps(response.json(), indent=2))
        
        # Teste 4: Usar saudador
        print("\n4. Testando saudador...")
        response = requests.post(
            f"{baseUrl}/tools/greeter",
            json={"name": "Ana"}
        )
        print(json.dumps(response.json(), indent=2))
        
        # Teste 5: Health check
        print("\n5. Testando health check...")
        response = requests.get(f"{base_url}/health")
        print(json.dumps(response.json(), indent=2))
        
    except Exception as e:
        print(f"Erro nos testes: {e}")

if __name__ == "__main__":
    test_mcp_server()
```

Execute com:
```bash
python test.py
```

## 🔧 Testando Erros

### Requisição inválida para calculadora:
```bash
curl -X POST http://localhost:3000/tools/calculator \
  -H "Content-Type: application/json" \
  -d '{"expression": "invalid"}'
```

### Método não permitido:
```bash
curl -X PUT http://localhost:3000/tools
```

### Ferramenta não encontrada:
```bash
curl -X POST http://localhost:3000/tools/inexistent \
  -H "Content-Type: application/json" \
  -d '{"data": "test"}'
```

## 📊 Monitoramento

Para monitorar o servidor em tempo real, você pode usar:

```bash
# Monitorar logs
tail -f /var/log/mcp-server.log

# Verificar se o servidor está rodando
curl -s http://localhost:3000/health | jq '.status'

# Teste de carga simples
for i in {1..10}; do
  curl -s http://localhost:3000/health > /dev/null
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