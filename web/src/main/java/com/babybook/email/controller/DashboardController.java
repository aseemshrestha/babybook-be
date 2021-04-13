package com.babybook.email.controller;

import com.babybook.email.constans.ApiBaseUrl;
import com.babybook.email.exceptions.DataNotAvailableException;
import com.babybook.email.model.Baby;
import com.babybook.email.model.User;
import com.babybook.email.validations.ValidationService;
import com.babybook.email.views.Views;
import com.babybook.service.BabyService;
import com.babybook.service.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;

@RestController
@RequestMapping( ApiBaseUrl.API )

public class DashboardController
{
    private final BabyService babyService;
    private final ValidationService validationService;
    private final UserService userService;

    public DashboardController(BabyService babyService,
        ValidationService validationService, UserService userService)
    {
        this.babyService = babyService;
        this.validationService = validationService;
        this.userService = userService;
    }

    @GetMapping( "v1/secured/get-dashboard" )
    public ResponseEntity<?> getDashboard()
    {
        return new ResponseEntity<>("Dashboard", HttpStatus.OK);
    }

    @PostMapping( "v1/secured/submit-baby-details" )
    public ResponseEntity<Baby> submitBabyDetails(@RequestBody @Valid Baby baby, HttpServletRequest request)
    {
        baby.setLastUpdated(new Date());
        baby.setCreated(new Date());
        User user = userService.getUser(request.getUserPrincipal().getName()).get();
        baby.setUser(user);
        Baby savedBaby = this.babyService.saveBaby(baby);
        return new ResponseEntity<>(savedBaby, HttpStatus.OK);
    }

    @JsonView( Views.UserOnlyView.class )
    @GetMapping( "v1/secured/get-baby-by-username/{username}" )
    public ResponseEntity<List<Baby>> getBaby(@PathVariable( "username" ) @NotNull String username,
        HttpServletRequest request)
    {
        this.validationService.isLoggedUserValid(username, request);
        Optional<List<Baby>> baby = this.babyService.findBabyByUserName(username);
        if (!baby.isPresent()) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }
        return new ResponseEntity<>(baby.get(), HttpStatus.OK);
    }

    @JsonView( Views.BabyWithUserView.class )
    @GetMapping( "v1/secured/get-baby-by-id/{id}" )
    public ResponseEntity<Baby> getBabyById(@PathVariable( "id" ) @NotNull Long id)
    {
        Optional<Baby> baby = this.babyService.findBabyById(id);
        if (!baby.isPresent()) {
            throw new DataNotAvailableException("NO_DATA_AVAILABLE");
        }
        return new ResponseEntity<>(baby.get(), HttpStatus.OK);
    }

}
