package com.github.morihara.transactional.sample.executor;

import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import com.github.morihara.transactional.sample.config.MainConfig;
import com.github.morihara.transactional.sample.enumeration.ServiceCodeEnum;
import com.github.morihara.transactional.sample.service.GoodsIssueService;
import com.github.morihara.transactional.sample.service.GoodsReceiptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Import({MainConfig.class})
@Slf4j
public class MainApplicationRunner implements CommandLineRunner {
    private final GoodsIssueService goodsIssueService;
    private final GoodsReceiptService goodsReceiptService;

    @Override
    public void run(String... arg0) throws Exception {
        log.info("----start----");
        String serviceCode = arg0[0];
        String quantityCode = arg0[1];
        BigDecimal quantity = new BigDecimal(arg0[2]);
        log.info("service_code: {}", serviceCode);
        log.info("quantity_code: {}", quantityCode);
        log.info("quantity: {}", quantity);

        if (quantity.compareTo(BigDecimal.ZERO) < 0) {
            log.error("Quantity cannot be set a negative number.");
            return;
        }

        switch (ServiceCodeEnum.getEnumName(serviceCode)) {
            case GOODS_ISSUE:
                goodsIssueService.registerNewGoodsIssue(serviceCode, quantityCode, quantity);
                break;
            case GOODS_RECEIPT:
                goodsReceiptService.registerNewGoodsReceipt(serviceCode, quantityCode, quantity);
                break;
            default:
                break;
        }
        log.info("----end----");
    }

}
