package com.github.morihara.transactional.sample.service;

import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.github.morihara.transactional.sample.dao.GoodsIssueDao;
import com.github.morihara.transactional.sample.dto.GoodsIssueTrnDto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GoodsIssueServiceImpl implements GoodsIssueService {
    private final GoodsIssueDao issueDao;
    private final UpdateQuantityService updateQuantityService;

    @Override
    @Transactional
    public void registerNewGoodsIssue(String serviceCode, String quantityCode,
            BigDecimal quantity) {
        UUID newGoodsIssueTrnId = UUID.randomUUID();
        GoodsIssueTrnDto issueDto = GoodsIssueTrnDto.builder()
                .goodsIssueTrnId(newGoodsIssueTrnId).quantity(quantity).build();
        issueDao.insert(issueDto);
        updateQuantityService.decreaseQuantity(serviceCode, quantityCode, issueDto);
    }
}
