package com.github.morihara.transactional.sample.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import com.github.morihara.transactional.sample.dao.GoodsIssueDao;
import com.github.morihara.transactional.sample.dao.GoodsIssueDaoImpl;
import com.github.morihara.transactional.sample.dao.GoodsReceiptDao;
import com.github.morihara.transactional.sample.dao.GoodsReceiptDaoImpl;
import com.github.morihara.transactional.sample.dao.QuantityDao;
import com.github.morihara.transactional.sample.dao.QuantityDaoImpl;
import com.github.morihara.transactional.sample.dao.QuantityHistoryDao;
import com.github.morihara.transactional.sample.dao.QuantityHistoryDaoImpl;

@Configuration
public class DaoConfig {
    @Bean
    @Autowired
    public GoodsIssueDao goodsIssueDao(JdbcTemplate jdbc) {
        return new GoodsIssueDaoImpl(jdbc);
    }

    @Bean
    @Autowired
    public GoodsReceiptDao goodsReceiptDao(JdbcTemplate jdbc) {
        return new GoodsReceiptDaoImpl(jdbc);
    }

    @Bean
    @Autowired
    public QuantityDao quantityDao(JdbcTemplate jdbc) {
        return new QuantityDaoImpl(jdbc);
    }

    @Bean
    @Autowired
    public QuantityHistoryDao quantityHistoryDao(JdbcTemplate jdbc) {
        return new QuantityHistoryDaoImpl(jdbc);
    }
}
