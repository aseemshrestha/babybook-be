package com.babybook.email.model;

import annotations.ValidEnum;
import com.babybook.email.constans.Gender;
import com.babybook.email.constans.PostType;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Entity
@Table( name = "post" )
@Data
@NoArgsConstructor
public class Post extends BaseEntity
{
    @NotEmpty( message = "Title is required" )
    @Column( nullable = false )
    private String title;

    private String description;

    private String albumName;

    @OneToMany( cascade = CascadeType.ALL, mappedBy = "post", fetch = FetchType.LAZY )
    private List<Media> media;

    private String ip;

    private String browser;

    @ValidEnum( targetClassType = PostType.class, message = "Post type is not valid" )
    private String postType;

    @Column( nullable = false )
    private String postedBy;

    @OneToMany( cascade = CascadeType.ALL, mappedBy = "post", fetch = FetchType.LAZY )
    private List<Comment> comment;


}
