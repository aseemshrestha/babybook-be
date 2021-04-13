package com.babybook.service;

import com.babybook.email.model.User;
import com.babybook.repository.BabyRepository;
import com.babybook.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService
{
    private final UserRepository userRepository;
    private final BabyRepository babyRepository;

    public UserService(UserRepository userRepository, BabyRepository babyRepository)
    {
        this.userRepository = userRepository;
        this.babyRepository = babyRepository;
    }

    @Transactional( rollbackFor = Exception.class )
    public User saveUser(User user)
    {
        return this.userRepository.save(user);
    }

    @Transactional( rollbackFor = Exception.class )
    public List<User> saveAll(List<User> users)
    {
        return this.userRepository.saveAll(users);
    }

    public Optional<User> getUser(String username)
    {
        return this.userRepository.findByUsername(username);
    }

    @Transactional( rollbackFor = Exception.class )
    public int updatePassword(String username, String password)
    {
        return userRepository.updatePassword(username, password);
    }
}
