/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Configuration.java to edit this template
 */
package com.example.demo;

import java.io.File;
import java.nio.file.Paths;
import java.time.Duration;
import javax.annotation.PreDestroy;
import org.springframework.boot.devtools.filewatch.FileSystemWatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author PC
 */
@Configuration
//@EnableElasticsearchRepositories(basePackages = "com.baeldung.spring.data.es.repository")
//@ComponentScan(basePackages = { "com.baeldung.spring.data.es.service" })
public class ApplicationConfig {
    public static final String StorageFolder = "Output";
    public static final String InputFolder = "Uploads";
    public static final String DB_USER = "elastic";
    public static final String DB_PASSWORD = "PxBfFe5yQbAQQDLt0Xls";
    public static final String DB_URL = "localhost";
    public static final int DB_PORT = 9200;
    
    @Bean
    public FileSystemWatcher fileSystemWatcher() {
        FileSystemWatcher fileSystemWatcher = new FileSystemWatcher(true, Duration.ofMillis(5000L), Duration.ofMillis(3000L));
        fileSystemWatcher.addSourceDirectory(new File(Paths.get(InputFolder).toAbsolutePath().normalize().toString()));
        fileSystemWatcher.addListener(new MyFileChangeListener(new FileProcessor()));
        fileSystemWatcher.start();
        System.out.println("started fileSystemWatcher");
        return fileSystemWatcher;
    }
    
    @PreDestroy
        public void onDestroy() throws Exception {
        fileSystemWatcher().stop();
    }
        
//    @Bean
//    public RestHighLevelClient client() {
//        ClientConfiguration clientConfiguration 
//            = ClientConfiguration.builder()
//                .connectedTo("https://localhost:9200")
//                .build();
//
//        return RestClients.create(clientConfiguration).rest();
//    }
//
//    @Bean
//    public ElasticsearchOperations elasticsearchTemplate() {
//        return new ElasticsearchRestTemplate(client());
//    }
}
