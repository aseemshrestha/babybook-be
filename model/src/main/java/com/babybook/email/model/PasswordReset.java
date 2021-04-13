package com.babybook.email.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Data
public class PasswordReset extends BaseEntity
{
    @NotNull
    private String username;
    @NotNull
    private String resetCode;
    @Temporal( TemporalType.TIMESTAMP )
    private Date expiresAt;
}
