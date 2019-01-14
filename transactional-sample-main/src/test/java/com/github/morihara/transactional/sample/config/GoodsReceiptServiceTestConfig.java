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

import com.github.morihara.transactional.sample.dao.GoodsReceiptDao;
import com.github.morihara.transactional.sample.dao.GoodsReceiptDaoImpl;
import com.github.morihara.transactional.sample.dao.QuantityDao;
import com.github.morihara.transactional.sample.dao.QuantityDaoImpl;
import com.github.morihara.transactional.sample.dao.QuantityHistoryDao;
import com.github.morihara.transactional.sample.dao.QuantityHistoryDaoImpl;
import com.github.morihara.transactional.sample.dto.GoodsReceiptTrnDto;
import com.github.morihara.transactional.sample.exception.TransactionalRuntimeException;
import com.github.morihara.transactional.sample.service.GoodsReceiptService;
import com.github.morihara.transactional.sample.service.GoodsReceiptServiceImpl;
import com.github.morihara.transactional.sample.service.UpdateQuantityService;
import com.github.morihara.transactional.sample.service.UpdateQuantityServiceImpl;

@Configuration
@Import({
        UpdateQuantityServiceTestConfig.class,
        JDBCTestConfig.class
})
public class GoodsReceiptServiceTestConfig {
    @Bean
    @Autowired
    public GoodsReceiptDao goodsReceiptDao(JdbcTemplate jdbc) {
        return new GoodsReceiptDaoImpl(jdbc);
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
                .increaseQuantity(anyString(), anyString(), any(GoodsReceiptTrnDto.class));
        return updateQuantityServiceMock;
    }

    @Bean
    @Autowired
    @Qualifier("updateQuantityServiceForCommit")
    public GoodsReceiptService goodsReceiptServiceForCommit(GoodsReceiptDao goodsReceiptDao,
            UpdateQuantityService updateQuantityServiceForCommit) {
        return new GoodsReceiptServiceImpl(goodsReceiptDao, updateQuantityServiceForCommit);
    }

    @Bean
    @Autowired
    @Qualifier("updateQuantityServiceForRollback")
    public GoodsReceiptService goodsReceiptServiceForRollback(GoodsReceiptDao goodsReceiptDao,
            UpdateQuantityService updateQuantityServiceForRollback) {
        return new GoodsReceiptServiceImpl(goodsReceiptDao, updateQuantityServiceForRollback);
    }
}
