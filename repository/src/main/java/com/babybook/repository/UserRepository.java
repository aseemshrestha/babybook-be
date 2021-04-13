package com.babybook.repository;

import com.babybook.email.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>
{
    @Query( "SELECT u from User u where u.username = :username and u.isActive = 1" )
    Optional<User> findByUsername(String username);

    @Modifying( clearAutomatically = true )
    @Query( "Update User u set u.password =:password where u.username =:username and u.isActive = 1" )
    int updatePassword(String username, String password);


}
