package com.anhnt.customer.client;

import com.anhnt.common.domain.payment.request.TransactionCreateParam;
import com.anhnt.common.domain.payment.request.TransactionCreateRequest;
import com.anhnt.common.domain.payment.response.CancelCreateTransactionResponse;
import com.anhnt.common.domain.payment.response.ConfirmCreateTransactionResponse;
import com.anhnt.common.domain.payment.response.TryCreateTransactionResponse;
import com.anhnt.common.domain.response.BodyEntity;
import com.anhnt.customer.client.config.ConfigurationClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="payment",url="${feign.client.config.payment.url}",configuration = ConfigurationClientConfiguration.class)
public interface PaymentClient {
    @PostMapping(value = "/transactions", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    BodyEntity<TryCreateTransactionResponse> tryCreateTransaction(@SpringQueryMap TransactionCreateParam param, @RequestBody TransactionCreateRequest createOrderRequest);

    @PutMapping(value = "/transactions/{id}/confirm", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    BodyEntity<ConfirmCreateTransactionResponse> confirmCreateTransaction(@RequestParam Long id);

    @DeleteMapping(value = "/transactions/{id}/cancel", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    BodyEntity<CancelCreateTransactionResponse> cancelCreateTransaction(@RequestParam Long id);

}