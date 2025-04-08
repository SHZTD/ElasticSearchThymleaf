package com.example.elastic.logger;

import org.apache.http.HttpHost;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class VerLogs {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<List<String>> hacerQuery(String esqlQuery) {
        List<List<String>> logs = new ArrayList<>();

        try (RestClient client = RestClient.builder(new HttpHost("172.21.236.117", 9200, "http")).build()) {
            ObjectNode payload = objectMapper.createObjectNode();
            payload.put("query", esqlQuery);

            Request request = new Request("POST", "/_query");
            request.setJsonEntity(payload.toString());

            Response response = client.performRequest(request);
            String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

            JsonNode jsonResponse = objectMapper.readTree(responseBody);
            JsonNode valuesNode = jsonResponse.get("values");

            if (valuesNode != null && valuesNode.isArray()) {
                for (JsonNode row : valuesNode) {
                    List<String> entry = new ArrayList<>();
                    for (JsonNode value : row) {
                        entry.add(value.asText());
                    }
                    logs.add(entry);
                }
            }

        } catch (IOException e) {
            System.err.println("Error executing Elasticsearch query: " + esqlQuery);
            e.printStackTrace();
        }

        return logs;
    }
}