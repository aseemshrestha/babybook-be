package com.babybook.email.model;

import com.babybook.email.views.Views;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Data
@Builder
@AllArgsConstructor

public class Role implements Serializable
{
    @Id
    @JsonView( Views.UserOnlyView.class )
    private int id;
    @JsonView( Views.UserOnlyView.class )
    private String role;
    public Role() {}
}
