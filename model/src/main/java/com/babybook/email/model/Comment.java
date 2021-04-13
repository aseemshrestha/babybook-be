package com.babybook.email.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Comment extends BaseEntity
{
    private String comment;
    private String commentedBy;
    @JsonIgnore
    @ManyToOne( cascade = CascadeType.ALL )
    private Post post;

    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public String getCommentedBy()
    {
        return commentedBy;
    }

    public void setCommentedBy(String commentedBy)
    {
        this.commentedBy = commentedBy;
    }

    public Post getPost()
    {
        return post;
    }

    public void setPost(Post post)
    {
        this.post = post;
    }
}
