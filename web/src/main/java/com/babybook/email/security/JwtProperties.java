package com.babybook.email.security;

public class JwtProperties
{
    public static final String SECRET = "kathmandu";
    public static final Long EXPIRATION_TIME = 31536000000L; //
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";

}
