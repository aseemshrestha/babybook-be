package com.babybook.email.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@RestController
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler
{

    public final ResponseEntity handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
        HttpStatus status, WebRequest request)
    {

        List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(x -> x.getDefaultMessage())
            .collect(Collectors.toList());
        ApiException apiException =
            new ApiException(new Date(), errors, request.getDescription(false), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler( DataNotAvailableException.class )
    public final ResponseEntity dataNotAvailableException(Exception ex, WebRequest request)
    {
        ApiException apiException =
            new ApiException(new Date(), Arrays.asList(ex.getMessage()), request.getDescription(false),
                             HttpStatus.OK.value());
        return new ResponseEntity(apiException, HttpStatus.OK);
    }

    @ExceptionHandler( BadRequestException.class )
    public final ResponseEntity handleBadRequestException(Exception ex, WebRequest request)
    {
        ApiException apiException =
            new ApiException(new Date(), Arrays.asList(ex.getMessage()), request.getDescription(false),
                             HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity(apiException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler( DataIntegrityViolationException.class )
    protected ResponseEntity handleConstraintViolation(DataIntegrityViolationException ex, WebRequest request)
    {
        String msg = ex.getRootCause().getMessage();
        String outputMsg = "";
        if (msg.contains("Duplicate entry")) {
            switch (outputMsg = msg.contains("@") ? "Email is taken" : "User name is taken") {
            }
        }
        ApiException apiException =
            new ApiException(new Date(), Arrays.asList(outputMsg), request.getDescription(false),
                             HttpStatus.CONFLICT.value());
        return new ResponseEntity(apiException, HttpStatus.CONFLICT);
    }

    @ExceptionHandler( ResourceNotFoundException.class )
    public final ResponseEntity<Object> handleResourceNotFound(Exception ex, WebRequest request)
    {
        ApiException apiException =
            new ApiException(new Date(), Arrays.asList(ex.getMessage()), request.getDescription(false),
                             HttpStatus.NOT_FOUND.value());
        return new ResponseEntity(apiException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler( Exception.class )
    public final ResponseEntity<Object> handleAlException(Exception ex, WebRequest request)
    {
        ApiException apiException =
            new ApiException(new Date(), Arrays.asList(ex.getMessage()), request.getDescription(false),
                             HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity(apiException, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
