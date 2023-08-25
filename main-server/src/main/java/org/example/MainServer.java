package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MainServer {
    public static void main(String[] args) {
//        TODO: добавить @Transactional;
        SpringApplication.run(MainServer.class);
    }
}