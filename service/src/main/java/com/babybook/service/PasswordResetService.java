package com.babybook.service;

import com.babybook.email.model.PasswordReset;
import com.babybook.repository.PasswordResetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PasswordResetService
{
    private final PasswordResetRepository passwordResetRepository;

    public PasswordResetService(PasswordResetRepository passwordResetRepository)
    {
        this.passwordResetRepository = passwordResetRepository;
    }

    @Transactional( rollbackFor = Exception.class )
    public PasswordReset savePasswordReset(PasswordReset passwordReset)
    {
        return this.passwordResetRepository.save(passwordReset);
    }

    public Optional<PasswordReset> getPasswordReset(String code)
    {
        Optional<PasswordReset> byResetCode = this.passwordResetRepository.findByResetCode(code);
        return byResetCode;
    }
}
