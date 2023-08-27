package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//        TODO: добавить @Transactional;
// TODO убррать public с интерфейсов

@SpringBootApplication
public class MainServer {
    public static void main(String[] args) {
        SpringApplication.run(MainServer.class);
    }
}