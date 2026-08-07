package com.wk.knowledge;

import org.springframework.boot.SpringApplication;

public class TestTiKnowledgeApiApplication {

    public static void main(String[] args) {
        SpringApplication.from(Application::main).with(TestcontainersConfiguration.class).run(args);
    }

}
