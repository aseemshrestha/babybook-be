package com.babybook.email.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
@Data
public class BaseEntity implements Serializable
{
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private Long id;

    @Temporal( TemporalType.TIMESTAMP )
    @Column( name = "created", nullable = false, updatable = false )
    @CreatedDate
    private Date created;

    @Temporal( TemporalType.TIMESTAMP )
    @Column( name = "lastUpdated", nullable = false )
    @LastModifiedDate
    private Date lastUpdated;
}
