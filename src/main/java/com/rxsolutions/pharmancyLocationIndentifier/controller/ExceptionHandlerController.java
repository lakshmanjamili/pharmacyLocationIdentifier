package com.rxsolutions.pharmancyLocationIndentifier.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * All project root level exceptions needs to be captured example : if
 * parameters in the API are not provided as needed.
 */

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(value = { MissingServletRequestParameterException.class })
    public ResponseEntity<String> captureMissingParameterException(MissingServletRequestParameterException exception) {

        return new ResponseEntity<>(
                "Required parameters are missing, to calculate distance input parameters are required",
                HttpStatus.BAD_REQUEST);

    }

}