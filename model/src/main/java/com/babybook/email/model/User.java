package com.babybook.email.model;

import annotations.ValidEnum;
import com.babybook.email.constans.Gender;
import com.babybook.email.views.Views;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Table( name = "user", indexes = {
    @Index( name = "idxemail", columnList = "email", unique = true ),
    @Index( name = "idxusername", columnList = "username", unique = true ) } )
@Data
public class User extends BaseEntity
{

    @NotEmpty( message = "First Name is required" )
    @Column( nullable = false )
    @JsonView( Views.UserOnlyView.class )
    private String firstName;

    @NotEmpty( message = "Last Name is required" )
    @Column( nullable = false )
    @JsonView( Views.UserOnlyView.class )
    private String lastName;

    @Column( nullable = false, unique = true, length = 200 )
    @Length( max = 200 )
    @JsonView( Views.UserOnlyView.class )
    private String username;

    @NotEmpty( message = "Password is required" )
    @Column( nullable = false )
    @JsonIgnore
    @JsonProperty( access = JsonProperty.Access.WRITE_ONLY )
    private String password;

    @NotEmpty( message = "Email should be valid" )
    @Column( nullable = false, unique = true, length = 200 )
    @Length( max = 200 )
    @JsonView( Views.UserOnlyView.class )
    private String email;

    private String ip;
    private String browser;

    @JsonView( Views.UserOnlyView.class )
    private int isActive;

    @JsonView( Views.UserOnlyView.class )
    @ValidEnum( targetClassType = Gender.class, message = "Gender is empty or not valid" )
    private String gender;

    @ManyToOne
    @JoinColumn
    @JsonView( Views.UserOnlyView.class )
    private Role role;

    /*@Transient
    @OneToMany( mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL )
    private Set<Baby> babies;*/

}
