package com.babybook.service;

import com.babybook.email.model.Baby;
import com.babybook.repository.BabyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class BabyService
{
    private final BabyRepository babyRepository;

    public BabyService(BabyRepository babyRepository)
    {
        this.babyRepository = babyRepository;
    }

    @Transactional( rollbackFor = Exception.class )
    public Baby saveBaby(Baby baby)
    {
        return this.babyRepository.save(baby);
    }

    public Optional<List<Baby>> findBabyByUserName(String username)
    {
        return babyRepository.findBabyByUsername(username);
    }

    public Optional<Baby> findBabyById(Long id)
    {
        return babyRepository.findById(id);
    }

}
