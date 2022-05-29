package com.anhnt.customer.config.exception;

import com.anhnt.common.domain.apigateway.response.ErrorEntityConstant;
import com.anhnt.common.domain.exception.AbstractException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

// https://www.baeldung.com/rest-api-error-handling-best-practices
// https://google.github.io/styleguide/jsoncstyleguide.xml
// http://docs.oasis-open.org/odata/odata-json-format/v4.0/errata02/os/odata-json-format-v4.0-errata02-os-complete.html#_Toc403940655
@RestControllerAdvice
@Slf4j
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(Exception e) {
        return ErrorEntityConstant.INTERNAL_SERVER_ERROR.withParams(e.getMessage()).toResponseEntity();
    }

    @ExceptionHandler(AbstractException.class)
    public ResponseEntity handleException(AbstractException e) {
        return e.getError().toResponseEntity();
    }

}
