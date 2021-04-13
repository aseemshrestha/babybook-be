package com.babybook.email.constans;

public class AppConstants
{
    public static final String FORGOT_PASS_SUBJECT = "Password Reset Request";
    public static final String FORGOT_PASS_MESSAGE =
        "You recently requested for password reset. We are here to help.\n "
            + "Please use the code below to reset your password.\n ";
    public static final String FORGOT_PASS_VALID_MINS = "Please note above code is valid for only 24 hours.";
    public static final String APP_NAME = "Babybook";
    public static final String TEAM = "Team";

    public static final Integer RESET_PASS_EXPIRY_HOURS = 24;

    public static final Long MAX_IMAGE_UPLOAD_SIZE = 5242880L;
}
