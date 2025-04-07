package com.example.elastic.upload;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// clase que se utiliza para subir datos
public class SubirDatos {
    public void subir(String logfile) {
        try {
            ElasticsearchClient client = ElasticsearchConnection.createClient();
            BufferedReader br = new BufferedReader(new FileReader(logfile));
            String line;

            Pattern logPattern = Pattern.compile(
                    "^(\\S+) \\S+ \\S+ \\[(.*?)\\] \"(\\S+) (\\S+) \\S+\" (\\d+) (\\d+)"
            );

            while ((line = br.readLine()) != null) {
                Matcher matcher = logPattern.matcher(line);

                if (matcher.find()) {
                    Map<String, Object> logEntry = new HashMap<>();
                    logEntry.put("ip", matcher.group(1)); // ip
                    logEntry.put("timestamp", matcher.group(2)); // timestamp
                    logEntry.put("method", matcher.group(3)); // GET, POST...
                    logEntry.put("url", matcher.group(4)); // URL
                    logEntry.put("status", Integer.parseInt(matcher.group(5))); // HTTP
                    logEntry.put("size", Integer.parseInt(matcher.group(6))); // respuesta

                    // insertar en Elasticsearch
                    IndexRequest<Map<String, Object>> request = IndexRequest.of(i -> i
                            .index("logs_server")
                            .document(logEntry)
                    );
                    client.index(request);
                }
            }
            br.close();
            System.out.println("Todos los logs han sido enviados a Elasticsearch.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
