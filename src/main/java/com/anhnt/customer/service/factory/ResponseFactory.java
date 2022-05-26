package com.anhnt.customer.service.factory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class ResponseFactory {

    public ResponseEntity success() {
        return success(new HashMap<>());
    }

    public <T> ResponseEntity<ResponseBody<T>> success(T data) {
        ResponseBody<T> body = new ResponseBody();
        body.setData(data);
        return ResponseEntity.ok(body);
    }

    public ResponseEntity error(HttpStatus httpStatus, String code, String message) {
        ResponseBody<Object> body = new ResponseBody();
        body.setError(new ResponseError(code,message));
        return new ResponseEntity(body, httpStatus);
    }

}
