package com.anhnt.customer.client;

import com.anhnt.common.domain.payment.request.TransactionCreateParam;
import com.anhnt.common.domain.payment.request.TransactionCreateRequest;
import com.anhnt.customer.client.config.ConfigurationClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="payment",url="${feign.client.config.payment.url}",configuration = ConfigurationClientConfiguration.class)
public interface PaymentClient {
    @PostMapping(value = "/transactions", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    Long createTransaction(@SpringQueryMap TransactionCreateParam param, @RequestBody TransactionCreateRequest createOrderRequest);

}