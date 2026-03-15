package api.common.advice;

import api.response.ApiErrorResponse;
import exception.CommonException;
import exception.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

import static exception.Status.*;

@RestControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {
    @ExceptionHandler(CommonException.class)
    ResponseEntity<ApiErrorResponse> handleCommonException(CommonException exception){
        log.error("exception",exception);
        return new ResponseEntity<>(
                ApiErrorResponse.fromCommonException(exception)
                , exception.getHttpStatus());
    }

    @ExceptionHandler(IOException.class)
    ResponseEntity<ApiErrorResponse> handleIOException(){
        return new ResponseEntity<>(
                ApiErrorResponse.fromIOException()
                , UPLOAD_FAIL.getHttpStatus());
    }
}
