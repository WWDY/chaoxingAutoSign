package com.daiju.cloudsign.exception;

import com.daiju.cloudsign.controller.AdminController;
import com.daiju.cloudsign.result.ResultBody;
import com.daiju.cloudsign.result.ResultFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author WDY
 * @Date 2021-02-01 12:02
 * @Description TODO
 */
@RestControllerAdvice(basePackageClasses = {AdminController.class})
public class ValidException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultBody vaild(MethodArgumentNotValidException exception){
        BindingResult bindingResult = exception.getBindingResult();
        Map<String, Object> data = new HashMap<>();
        bindingResult.getFieldErrors().forEach(fieldError -> {
            data.put(fieldError.getField(), fieldError.getDefaultMessage());
        });
        return ResultFactory.fail(data);
    }

    @ExceptionHandler(Exception.class)
    public ResultBody unknownException(Exception e){
        String message = e.getMessage();
        return ResultFactory.fail(message);
    }
}
