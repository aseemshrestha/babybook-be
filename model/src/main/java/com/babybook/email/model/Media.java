package com.babybook.email.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class Media extends BaseEntity
{
    private String mediaType;
    private String mediaLocation;
    private String mediaDescription;
    @JsonIgnore
    @ManyToOne( cascade = CascadeType.ALL )
    private Post post;

    public String getMediaType()
    {
        return mediaType;
    }

    public void setMediaType(String mediaType)
    {
        this.mediaType = mediaType;
    }

    public String getMediaLocation()
    {
        //if (StringUtils.isNotBlank(mediaLocation)) {

          //  return mediaLocation.substring((mediaLocation.indexOf("uploads/")));
       // }
        return mediaLocation;
    }

    public void setMediaLocation(String mediaLocation)
    {
        this.mediaLocation = mediaLocation;
    }

    public String getMediaDescription()
    {
        return mediaDescription;
    }

    public void setMediaDescription(String mediaDescription)
    {
        this.mediaDescription = mediaDescription;
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
