package com.babybook.email.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class ApiException
{
    private Date timestamp;
    private List<String> message;
    private String details;
    private int status;
}
