package com.babybook.email.validations;

import com.babybook.email.exceptions.BadRequestException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class ValidationService
{
    public void isLoggedUserValid(String username, HttpServletRequest request)
    {
        String loggedInUser = request.getUserPrincipal().getName();

        if (!loggedInUser.equals(username)) {
            throw new BadRequestException("Bad Request with username:" + username);
        }
    }
}
