package com.nebarrow.weathertracker.service;

import com.nebarrow.weathertracker.dto.request.PostUser;
import com.nebarrow.weathertracker.dto.response.GetUser;
import com.nebarrow.weathertracker.exception.UserAlreadyExistsException;
import com.nebarrow.weathertracker.exception.UserNotFoundException;
import com.nebarrow.weathertracker.mapper.UserMapper;
import com.nebarrow.weathertracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public void create(PostUser postUser) {
        Optional<GetUser> existingUser = userRepository.findByLogin(postUser.login()).map(userMapper::toDto);
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("User with login " + postUser.login() + " already exists");
        }
        userRepository.save(userMapper.toUser(postUser));
    }

    public Optional<GetUser> findById(int id) {
        return userRepository.findById(id).map(userMapper::toDto);
    }

    public GetUser findByLogin(String login) {
        return userRepository.findByLogin(login).map(userMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException("User with login " + login + " not found"));
    }
}
