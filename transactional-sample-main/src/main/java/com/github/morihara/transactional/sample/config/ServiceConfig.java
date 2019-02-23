package com.github.morihara.transactional.sample.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import com.github.morihara.transactional.sample.dao.GoodsIssueDao;
import com.github.morihara.transactional.sample.dao.GoodsReceiptDao;
import com.github.morihara.transactional.sample.dao.QuantityDao;
import com.github.morihara.transactional.sample.dao.QuantityHistoryDao;
import com.github.morihara.transactional.sample.service.GoodsIssueService;
import com.github.morihara.transactional.sample.service.GoodsIssueServiceImpl;
import com.github.morihara.transactional.sample.service.GoodsReceiptService;
import com.github.morihara.transactional.sample.service.GoodsReceiptServiceImpl;
import com.github.morihara.transactional.sample.service.UpdateQuantityService;
import com.github.morihara.transactional.sample.service.UpdateQuantityServiceImpl;

@Configuration
@Import({DaoConfig.class})
public class ServiceConfig {
    @Bean
    @Autowired
    public UpdateQuantityService updateQuantityService(QuantityHistoryDao quantityHistoryDao,
            QuantityDao quantityDao) {
        return new UpdateQuantityServiceImpl(quantityHistoryDao, quantityDao);
    }

    @Bean
    @Autowired
    public GoodsIssueService goodsIssueService(GoodsIssueDao goodsIssueDao,
            UpdateQuantityService updateQuantityService) {
        return new GoodsIssueServiceImpl(goodsIssueDao, updateQuantityService);
    }

    @Bean
    @Autowired
    public GoodsReceiptService goodsReceiptService(GoodsReceiptDao goodsReceiptDao,
            UpdateQuantityService updateQuantityService) {
        return new GoodsReceiptServiceImpl(goodsReceiptDao, updateQuantityService);
    }
}
