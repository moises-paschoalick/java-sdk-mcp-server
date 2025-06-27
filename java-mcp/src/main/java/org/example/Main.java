package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;

/**
 * Exemplo simples de MCP Server usando Java SDK
 * Implementação simplificada que simula o comportamento do MCP usando STDIO
 */
public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {
        try {
            logger.info("🚀 Iniciando Java MCP Server...");
            logger.info("📋 Ferramentas disponíveis:");
            logger.info("   - calculator: Calculadora simples");
            logger.info("   - greeter: Saudação personalizada");
            logger.info("   - get_info: Informações do servidor");

            // Configurar entrada e saída
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));

            // Enviar handshake inicial
            sendHandshake(writer);

            // Loop principal para processar mensagens
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    JsonNode request = mapper.readTree(line);
                    processRequest(request, writer);
                } catch (Exception e) {
                    logger.error("Erro ao processar requisição: " + e.getMessage());
                    sendError(writer, "Erro interno do servidor", e.getMessage());
                }
            }

        } catch (Exception e) {
            logger.error("❌ Erro ao iniciar o MCP Server: " + e.getMessage(), e);
            System.exit(1);
        }
    }

    private static void sendHandshake(BufferedWriter writer) throws IOException {
        ObjectNode handshake = mapper.createObjectNode();
        handshake.put("jsonrpc", "2.0");
        handshake.put("method", "initialize");
        handshake.put("id", 1);
        
        ObjectNode params = mapper.createObjectNode();
        params.put("protocolVersion", "2024-11-05");
        params.put("capabilities", mapper.createObjectNode()
            .put("tools", true)
            .put("logging", true));
        params.put("clientInfo", mapper.createObjectNode()
            .put("name", "java-mcp-example")
            .put("version", "1.0.0"));
        
        handshake.set("params", params);
        
        writer.write(handshake.toString() + "\n");
        writer.flush();
    }

    private static void processRequest(JsonNode request, BufferedWriter writer) throws IOException {
        String method = request.get("method").asText();
        int id = request.get("id").asInt();

        switch (method) {
            case "tools/list":
                handleToolsList(request, writer, id);
                break;
            case "tools/call":
                handleToolCall(request, writer, id);
                break;
            case "initialize":
                handleInitialize(request, writer, id);
                break;
            default:
                sendError(writer, "Método não suportado: " + method, null);
        }
    }

    private static void handleInitialize(JsonNode request, BufferedWriter writer, int id) throws IOException {
        ObjectNode response = mapper.createObjectNode();
        response.put("jsonrpc", "2.0");
        response.put("id", id);
        
        ObjectNode result = mapper.createObjectNode();
        result.put("protocolVersion", "2024-11-05");
        result.put("capabilities", mapper.createObjectNode()
            .put("tools", true)
            .put("logging", true));
        result.put("serverInfo", mapper.createObjectNode()
            .put("name", "java-mcp-example")
            .put("version", "1.0.0"));
        
        response.set("result", result);
        
        writer.write(response.toString() + "\n");
        writer.flush();
    }

    private static void handleToolsList(JsonNode request, BufferedWriter writer, int id) throws IOException {
        ObjectNode response = mapper.createObjectNode();
        response.put("jsonrpc", "2.0");
        response.put("id", id);
        
        ObjectNode result = mapper.createObjectNode();
        result.set("tools", mapper.createArrayNode()
            .add(createToolSpec("calculator", "Calculadora simples que avalia expressões matemáticas básicas", 
                Map.of("expression", "Expressão matemática (ex: 2 + 3)")))
            .add(createToolSpec("greeter", "Saudação personalizada", 
                Map.of("name", "Nome da pessoa para saudar")))
            .add(createToolSpec("get_info", "Obtém informações sobre o servidor MCP", 
                Map.of())));
        
        response.set("result", result);
        
        writer.write(response.toString() + "\n");
        writer.flush();
    }

    private static ObjectNode createToolSpec(String name, String description, Map<String, String> parameters) {
        ObjectNode tool = mapper.createObjectNode();
        tool.put("name", name);
        tool.put("description", description);
        
        ObjectNode params = mapper.createObjectNode();
        for (Map.Entry<String, String> param : parameters.entrySet()) {
            params.put(param.getKey(), param.getValue());
        }
        tool.set("inputSchema", params);
        
        return tool;
    }

    private static void handleToolCall(JsonNode request, BufferedWriter writer, int id) throws IOException {
        ObjectNode response = mapper.createObjectNode();
        response.put("jsonrpc", "2.0");
        response.put("id", id);
        
        JsonNode params = request.get("params");
        String toolName = params.get("name").asText();
        JsonNode arguments = params.get("arguments");
        
        ObjectNode result = mapper.createObjectNode();
        
        try {
            switch (toolName) {
                case "calculator":
                    result.set("content", handleCalculator(arguments));
                    break;
                case "greeter":
                    result.set("content", handleGreeter(arguments));
                    break;
                case "get_info":
                    result.set("content", handleGetInfo());
                    break;
                default:
                    sendError(writer, "Ferramenta não encontrada: " + toolName, null);
                    return;
            }
        } catch (Exception e) {
            sendError(writer, "Erro ao executar ferramenta: " + e.getMessage(), null);
            return;
        }
        
        response.set("result", result);
        writer.write(response.toString() + "\n");
        writer.flush();
    }

    private static ObjectNode handleCalculator(JsonNode arguments) {
        String expression = arguments.get("expression").asText();
        double result = evaluateExpression(expression);
        
        ObjectNode content = mapper.createObjectNode();
        content.put("expression", expression);
        content.put("result", result);
        content.put("message", "Cálculo realizado com sucesso");
        
        return content;
    }

    private static ObjectNode handleGreeter(JsonNode arguments) {
        String name = arguments.has("name") ? arguments.get("name").asText() : "Visitante";
        if (name == null || name.trim().isEmpty()) {
            name = "Visitante";
        }
        
        ObjectNode content = mapper.createObjectNode();
        content.put("message", "Olá, " + name + "! Bem-vindo ao Java MCP Server!");
        content.put("timestamp", System.currentTimeMillis());
        content.put("greeted_name", name);
        
        return content;
    }

    private static ObjectNode handleGetInfo() {
        ObjectNode content = mapper.createObjectNode();
        content.put("name", "Java MCP Server Example");
        content.put("version", "1.0.0");
        content.put("description", "Exemplo simples de MCP Server usando Java SDK");
        content.put("java_version", System.getProperty("java.version"));
        content.put("timestamp", System.currentTimeMillis());
        content.set("available_tools", mapper.createArrayNode()
            .add("calculator")
            .add("greeter")
            .add("get_info"));
        
        return content;
    }

    private static void sendError(BufferedWriter writer, String message, String details) throws IOException {
        ObjectNode error = mapper.createObjectNode();
        error.put("jsonrpc", "2.0");
        ObjectNode errorObj = mapper.createObjectNode()
            .put("code", -32603)
            .put("message", message);
        if (details != null) {
            errorObj.put("data", details);
        }
        error.set("error", errorObj);
        
        writer.write(error.toString() + "\n");
        writer.flush();
    }

    private static double evaluateExpression(String expression) {
        // Implementação básica para expressões simples
        expression = expression.replaceAll("\\s+", "");
        
        if (expression.contains("+")) {
            String[] parts = expression.split("\\+");
            return Double.parseDouble(parts[0]) + Double.parseDouble(parts[1]);
        } else if (expression.contains("-")) {
            String[] parts = expression.split("-");
            return Double.parseDouble(parts[0]) - Double.parseDouble(parts[1]);
        } else if (expression.contains("*")) {
            String[] parts = expression.split("\\*");
            return Double.parseDouble(parts[0]) * Double.parseDouble(parts[1]);
        } else if (expression.contains("/")) {
            String[] parts = expression.split("/");
            return Double.parseDouble(parts[0]) / Double.parseDouble(parts[1]);
        } else {
            return Double.parseDouble(expression);
        }
    }
}