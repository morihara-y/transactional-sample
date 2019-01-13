package com.github.morihara.transactional.sample.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"com.github.morihara.transactional.sample.dao",
        "com.github.morihara.transactional.sample.service",
        "com.github.morihara.transactional.sample.executor"})
public class MainConfig {
}
