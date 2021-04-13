package com.babybook.email.model;

import annotations.ValidEnum;
import com.babybook.email.constans.Gender;
import com.babybook.email.views.Views;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table( name = "baby" )
@Data
@NoArgsConstructor
public class Baby implements Serializable
{

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    @JsonView( Views.UserOnlyView.class )
    private Long id;

    @NotEmpty( message = "First Name is required" )
    @Column( nullable = false )
    @JsonView( Views.UserOnlyView.class )
    private String firstName;

    @NotEmpty( message = "Last Name is required" )
    @Column( nullable = false )
    @JsonView( Views.UserOnlyView.class )
    private String lastName;

    @Temporal( TemporalType.DATE )
    @Column( name = "dob", nullable = false )
    @NotNull( message = "Dob is required" )
    @JsonView( Views.UserOnlyView.class )
    private Date dob;

    @ValidEnum( targetClassType = Gender.class, message = "Gender is empty or not valid" )
    @JsonView( Views.UserOnlyView.class )
    private String gender;

    @Temporal( TemporalType.TIMESTAMP )
    @Column( name = "created", nullable = false, updatable = false )
    @CreatedDate
    @JsonView( Views.UserOnlyView.class )
    private Date created;

    @Temporal( TemporalType.TIMESTAMP )
    @Column( name = "lastUpdated", nullable = false )
    @LastModifiedDate
    @JsonView( Views.UserOnlyView.class )
    private Date lastUpdated;

    @ManyToOne( fetch = FetchType.LAZY, optional = false )
    @JoinColumn( name = "user_username", referencedColumnName = "username", nullable = false )
    @JsonView( Views.BabyWithUserView.class )
    private User user;

}
