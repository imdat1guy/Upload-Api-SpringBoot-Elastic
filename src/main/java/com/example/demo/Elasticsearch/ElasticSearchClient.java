/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.demo.Elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.example.demo.ApplicationConfig;
import com.example.demo.Models.Archive;
import com.example.demo.Models.ProcessedFile;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

/**
 *
 * @author PC
 */
public class ElasticSearchClient {
    private ElasticsearchClient client;
    public ElasticSearchClient(){
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(ApplicationConfig.DB_USER, ApplicationConfig.DB_PASSWORD));
        // Create the low-level client
        RestClient restClient = RestClient
                .builder(new HttpHost(ApplicationConfig.DB_URL, ApplicationConfig.DB_PORT, "https"))
                .setHttpClientConfigCallback((HttpAsyncClientBuilder httpClientBuilder) -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider))
                .build();

        // Create the transport with a Jackson mapper
        ElasticsearchTransport transport = new RestClientTransport(
            restClient, new JacksonJsonpMapper());

        // And create the API client
        client = new ElasticsearchClient(transport);
    }
    
    //

    /**
     *method to index a document. .
     * @param index index id
     * @param json json content
     * @return document id
     */
    public String saveDocument(String index, String json){
        try{
            Reader input = new StringReader(json);
            System.out.println(json);

            IndexRequest<JsonData> request = IndexRequest.of(i -> i
                .index(index)
                .withJson(input)
            );

            IndexResponse response = client.index(request);
            
            String id = response.id();
            System.out.println(id + "\t" + response.index() + "\t" + response.result().jsonValue());
            return id;
        }
        catch(ElasticsearchException | IOException ex){
            System.out.println(ex.getMessage());
            return "";
        }
    }
   
}
