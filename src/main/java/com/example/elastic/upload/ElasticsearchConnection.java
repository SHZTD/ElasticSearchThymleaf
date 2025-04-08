package com.example.elastic.upload;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

// para establecer una conexion a E/S
public class ElasticsearchConnection {
    public static ElasticsearchClient createClient() {
        RestClient restClient = RestClient.builder(
                new HttpHost("172.21.236.117", 9200, "http")
        ).build();

        RestClientTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper()
        );

        return new ElasticsearchClient(transport);
    }
}
