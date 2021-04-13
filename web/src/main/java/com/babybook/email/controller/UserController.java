package com.babybook.email.controller;

import com.babybook.email.constans.*;
import com.babybook.email.email.Mail;
import com.babybook.email.email.MailService;
import com.babybook.email.exceptions.BadRequestException;
import com.babybook.email.exceptions.ResourceNotFoundException;
import com.babybook.email.model.ForgotPassword;
import com.babybook.email.model.PasswordReset;
import com.babybook.email.model.PasswordResetSubmit;
import com.babybook.email.model.User;
import com.babybook.service.PasswordResetService;
import com.babybook.service.UserService;
import com.babybook.email.utils.AppUtils;
import com.babybook.email.utils.PasswordGenerator;
import com.babybook.email.validations.ValidationService;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.babybook.email.constans.AppConstants.RESET_PASS_EXPIRY_HOURS;

@RestController
@RequestMapping( ApiBaseUrl.API )
public class UserController
{
    private final UserService userService;
    private final ValidationService validationService;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final PasswordResetService passwordResetService;

    public UserController(UserService userService, ValidationService validationService, PasswordEncoder passwordEncoder,
        MailService mailService, PasswordResetService passwordResetService)
    {

        this.userService = userService;
        this.validationService = validationService;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
        this.passwordResetService = passwordResetService;
    }

    @PostMapping( "v1/public/create-user" )
    public ResponseEntity<User> createUser(@RequestBody @Valid User user, HttpServletRequest request)
    {
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        String ip = request.getHeader("X-FORWARDED-FOR");
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        user.setIp(ip);
        user.setLastUpdated(new Date());
        user.setUsername(user.getEmail());
        user.setBrowser(userAgent.getBrowser().getName() + "-" + userAgent.getOperatingSystem());
        user.setRole(RoleBuilder.getRoleRegisteredUser());
        user.setCreated(new Date());
        user.setIsActive(UserStatus.ACTIVE.getValue());
        User userSaved = this.userService.saveUser(user);
        return new ResponseEntity<>(userSaved, HttpStatus.CREATED);
    }

    @GetMapping( "v1/secured/get-user/{username}" )
    public ResponseEntity<User> getUser(@PathVariable( "username" ) @NotNull String username,
        HttpServletRequest request)
    {
        this.validationService.isLoggedUserValid(username, request);
        Optional<User> user = this.userService.getUser(username);
        if (!user.isPresent()) {
            throw new ResourceNotFoundException("User not found. Please try again");
        }
        return new ResponseEntity<>(user.get(), HttpStatus.OK);
    }

    @PostMapping( "v1/public/password-reset" )
    public ResponseEntity<?> forgotPassword(@RequestBody @NotNull ForgotPassword passReset, HttpServletRequest request)
        throws Exception
    {

        Optional<User> user = this.userService.getUser(passReset.getEmail());
        if (!user.isPresent()) {
            throw new ResourceNotFoundException("User not found. Please try again");
        }
        String sixDigitNumber = PasswordGenerator.generateSixDigitNumbers();
        Mail mail =
            Mail.builder().toEmail(passReset.getEmail()).subject(AppConstants.FORGOT_PASS_SUBJECT)
                .name(user.get().getFirstName())
                .username(user.get().getUsername())
                .randomCode(sixDigitNumber)
                .message(AppConstants.FORGOT_PASS_MESSAGE).build();

        PasswordReset pr = new PasswordReset();
        pr.setUsername(user.get().getUsername());
        pr.setResetCode(sixDigitNumber);
        pr.setExpiresAt(AppUtils.addHours(new Date(), RESET_PASS_EXPIRY_HOURS));
        pr.setCreated(new Date());
        pr.setLastUpdated(new Date());

        try {
            this.mailService.sendMail(mail, MailType.PASSWORD_RESET);
            this.passwordResetService.savePasswordReset(pr);
        } catch (Exception ex) {
            throw new Exception("error" + ex);
        }
        return new ResponseEntity<>("Resent link has been successfully sent to your email address.", HttpStatus.OK);
    }

    @PostMapping( "v1/public/password-reset-submit" )
    public ResponseEntity<?> passwordResetSubmit(@RequestBody @Valid PasswordResetSubmit passwordResetSubmit)
    {
        if (!passwordResetSubmit.getPassword().equals(passwordResetSubmit.getConfirmPassword())) {
            throw new BadRequestException("Password and Confirm password do not match.");
        }
        Optional<PasswordReset> passwordReset =
            passwordResetService.getPasswordReset(passwordResetSubmit.getResetCode());
        if (!passwordReset.isPresent()) {
            throw new BadRequestException("Reset Code not found. Please try again");
        }
        if (!passwordResetSubmit.getUsername().equals(passwordReset.get().getUsername())) {
            throw new BadRequestException("Username doesn't match. Please try again");
        }
        User user = userService.getUser(passwordResetSubmit.getUsername()).get();
        if (passwordEncoder.matches(passwordResetSubmit.getPassword(), user.getPassword())) {
            throw new BadRequestException("You cannot use the same password you have used before.");
        }
        long duration = new Date().getTime() - passwordReset.get().getExpiresAt().getTime();
        long hoursDiff = TimeUnit.MILLISECONDS.toHours(duration);
        if (hoursDiff > RESET_PASS_EXPIRY_HOURS) {
            throw new ResourceNotFoundException(
                "Reset Code is expired. Please resubmit the password reset request.");
        }
        this.userService.updatePassword(passwordReset.get().getUsername(),
                                        passwordEncoder.encode(passwordResetSubmit.getPassword()));

        return new ResponseEntity<>("Password Successfully updated. Please relogin with new password.", HttpStatus.OK);

    }

}
