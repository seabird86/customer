package com.anhnt.customer.client;

import com.anhnt.common.domain.payment.request.TransactionCreateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(url="http://localhost:8022/payment",value="payment")
public interface PaymentClient {
    @PostMapping(value = "/transactions", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    Long createTransaction(@RequestBody TransactionCreateRequest createOrderRequest);

}