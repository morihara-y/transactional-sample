package com.github.morihara.transactional.sample.config;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.github.morihara.transactional.sample.dao.GoodsReceiptDao;
import com.github.morihara.transactional.sample.dao.GoodsReceiptDaoImpl;
import com.github.morihara.transactional.sample.dto.GoodsReceiptTrnDto;
import com.github.morihara.transactional.sample.exception.TransactionalRuntimeException;
import com.github.morihara.transactional.sample.service.GoodsReceiptService;
import com.github.morihara.transactional.sample.service.GoodsReceiptServiceImpl;
import com.github.morihara.transactional.sample.service.UpdateQuantityService;

@Configuration
public class GoodsReceiptServiceTestConfig {
    @Bean
    @Autowired
    public GoodsReceiptDao goodsReceiptDao(JdbcTemplate jdbc) {
        return new GoodsReceiptDaoImpl(jdbc);
    }

    @Bean
    public UpdateQuantityService updateQuantityServiceMock() {
        UpdateQuantityService updateQuantityServiceMock = mock(UpdateQuantityService.class);
        doThrow(new TransactionalRuntimeException()).when(updateQuantityServiceMock)
                .increaseQuantity(anyString(), anyString(), any(GoodsReceiptTrnDto.class));
        return updateQuantityServiceMock;
    }

    @Bean
    @Autowired
    public GoodsReceiptService goodsReceiptService(GoodsReceiptDao goodsReceiptDao,
            UpdateQuantityService updateQuantityServiceMock) {
        return new GoodsReceiptServiceImpl(goodsReceiptDao, updateQuantityServiceMock);
    }
}
