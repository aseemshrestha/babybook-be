package com.babybook.email.controller;

import com.babybook.email.constans.ApiBaseUrl;
import com.babybook.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping( ApiBaseUrl.API )
public class AdminController
{
    private final UserService userService;

    public AdminController(UserService userService)
    {
        this.userService = userService;
    }

}
