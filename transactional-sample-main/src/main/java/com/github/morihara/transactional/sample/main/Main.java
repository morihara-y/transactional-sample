package com.github.morihara.transactional.sample.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.github.morihara.transactional.sample.executor.MainApplicationRunner;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(MainApplicationRunner.class, args);
    }

}
