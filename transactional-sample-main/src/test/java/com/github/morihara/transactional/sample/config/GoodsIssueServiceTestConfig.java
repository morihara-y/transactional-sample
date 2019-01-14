package com.github.morihara.transactional.sample.config;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

import com.github.morihara.transactional.sample.dao.GoodsIssueDao;
import com.github.morihara.transactional.sample.dao.GoodsIssueDaoImpl;
import com.github.morihara.transactional.sample.dao.QuantityDao;
import com.github.morihara.transactional.sample.dao.QuantityDaoImpl;
import com.github.morihara.transactional.sample.dao.QuantityHistoryDao;
import com.github.morihara.transactional.sample.dao.QuantityHistoryDaoImpl;
import com.github.morihara.transactional.sample.dto.GoodsIssueTrnDto;
import com.github.morihara.transactional.sample.exception.TransactionalRuntimeException;
import com.github.morihara.transactional.sample.service.GoodsIssueService;
import com.github.morihara.transactional.sample.service.GoodsIssueServiceImpl;
import com.github.morihara.transactional.sample.service.UpdateQuantityService;
import com.github.morihara.transactional.sample.service.UpdateQuantityServiceImpl;

@Configuration
@Import({
        JDBCTestConfig.class
})
public class GoodsIssueServiceTestConfig {
    @Bean
    @Autowired
    public GoodsIssueDao goodsIssueDao(JdbcTemplate jdbc) {
        return new GoodsIssueDaoImpl(jdbc);
    }

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

    @Bean
    @Autowired
    public UpdateQuantityService updateQuantityServiceForCommit(QuantityHistoryDao quantityHistoryDao,
            QuantityDao quantityDao) {
        return new UpdateQuantityServiceImpl(quantityHistoryDao, quantityDao);
    }

    @Bean
    public UpdateQuantityService updateQuantityServiceForRollback() {
        UpdateQuantityService updateQuantityServiceMock = mock(UpdateQuantityService.class);
        doThrow(new TransactionalRuntimeException()).when(updateQuantityServiceMock)
                .decreaseQuantity(anyString(), anyString(), any(GoodsIssueTrnDto.class));
        return updateQuantityServiceMock;
    }

    @Bean
    @Autowired
    @Qualifier("updateQuantityServiceForCommit")
    public GoodsIssueService goodsIssueServiceForCommit(GoodsIssueDao goodsIssueDao,
            UpdateQuantityService updateQuantityServiceForCommit) {
        return new GoodsIssueServiceImpl(goodsIssueDao, updateQuantityServiceForCommit);
    }

    @Bean
    @Autowired
    @Qualifier("updateQuantityServiceForRollback")
    public GoodsIssueService goodsIssueServiceForRollback(GoodsIssueDao goodsIssueDao,
            UpdateQuantityService updateQuantityServiceForRollback) {
        return new GoodsIssueServiceImpl(goodsIssueDao, updateQuantityServiceForRollback);
    }
}
