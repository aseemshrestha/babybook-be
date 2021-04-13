package com.babybook.repository;

import com.babybook.email.model.PasswordReset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PasswordResetRepository extends JpaRepository<PasswordReset, Long>
{
    @Query( "SELECT u from PasswordReset u where u.resetCode = :code" )
    Optional<PasswordReset> findByResetCode(String code);
}
