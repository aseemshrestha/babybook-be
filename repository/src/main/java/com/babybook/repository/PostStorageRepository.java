package com.babybook.repository;

import com.babybook.email.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostStorageRepository extends JpaRepository<Post, Long>
{
    @Query( "SELECT p from Post p where p.postedBy = :username order by p.lastUpdated desc" )
    Optional<List<Post>> findPostByUsername(String username);

}
