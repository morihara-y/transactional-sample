package com.github.morihara.transactional.sample.config;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

import com.github.morihara.transactional.sample.dao.QuantityDao;
import com.github.morihara.transactional.sample.dao.QuantityDaoImpl;
import com.github.morihara.transactional.sample.dao.QuantityHistoryDao;
import com.github.morihara.transactional.sample.dao.QuantityHistoryDaoImpl;
import com.github.morihara.transactional.sample.dto.QuantityHistoryTrnDto;
import com.github.morihara.transactional.sample.service.UpdateQuantityService;
import com.github.morihara.transactional.sample.service.UpdateQuantityServiceImpl;;

@Configuration
@Import({
        JDBCTestConfig.class
})
public class UpdateQuantityServiceTestConfig {
    @Bean
    @Autowired
    public QuantityHistoryDao quantityHistoryDaoForCommit(JdbcTemplate jdbc) {
        return new QuantityHistoryDaoImpl(jdbc);
    }

    @Bean
    public QuantityHistoryDao quantityHistoryDaoForRollback() {
        QuantityHistoryDao quantityHistoryDaoForRollback = mock(QuantityHistoryDao.class);
        doThrow(new RuntimeException()).when(quantityHistoryDaoForRollback)
                .insert(any(QuantityHistoryTrnDto.class));
        return quantityHistoryDaoForRollback;
    }

    @Bean
    @Autowired
    public QuantityDao quantityDao(JdbcTemplate jdbc) {
        return new QuantityDaoImpl(jdbc);
    }

    @Bean
    @Autowired
    @Qualifier("quantityHistoryDaoForCommit")
    public UpdateQuantityService updateQuantityServiceForCommit(QuantityHistoryDao quantityHistoryDaoForCommit,
            QuantityDao quantityDao) {
        return new UpdateQuantityServiceImpl(quantityHistoryDaoForCommit, quantityDao);
    }

    @Bean
    @Autowired
    @Qualifier("quantityHistoryDaoForRollback")
    public UpdateQuantityService updateQuantityServiceForRollback(QuantityHistoryDao quantityHistoryDaoForRollback,
            QuantityDao quantityDao) {
        return new UpdateQuantityServiceImpl(quantityHistoryDaoForRollback, quantityDao);
    }
}
