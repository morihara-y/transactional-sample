package com.github.morihara.transactional.sample.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

import com.github.morihara.transactional.sample.dao.QuantityDao;
import com.github.morihara.transactional.sample.dao.QuantityDaoImpl;
import com.github.morihara.transactional.sample.dao.QuantityHistoryDao;
import com.github.morihara.transactional.sample.dao.QuantityHistoryDaoImpl;

@Configuration
@Import({
        JDBCTestConfig.class
})
public class UpdateQuantityServiceTestConfig {
    @Bean
    @Autowired
    public QuantityHistoryDao quantityHistoryDao(JdbcTemplate jdbc) {
        return new QuantityHistoryDaoImpl(jdbc);
    }

    @Bean
    @Autowired
    public QuantityDao quantityDao(JdbcTemplate jdbc) {
        return new QuantityDaoImpl(jdbc);
    }
}
