package com.nebarrow.weathertracker.service;

import com.nebarrow.weathertracker.dto.request.PostUser;
import com.nebarrow.weathertracker.dto.response.GetUser;
import com.nebarrow.weathertracker.exception.UserAlreadyExistsException;
import com.nebarrow.weathertracker.exception.UserNotFoundException;
import com.nebarrow.weathertracker.mapper.UserMapper;
import com.nebarrow.weathertracker.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public GetUser create(PostUser postUser) {
        Optional<GetUser> existingUser = userRepository.findByLogin(postUser.login()).map(userMapper::toDto);
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("User with login " + postUser.login() + " already exists");
        }
        var user = userMapper.toUser(postUser);
        return userMapper.toDto(userRepository.save(user));
    }

    @Transactional
    public GetUser findById(int id) {
        return userRepository.findById(id).map(userMapper::toDto).orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
    }

    @Transactional
    public GetUser findByLogin(String login) {
        return userRepository.findByLogin(login).map(userMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException("User with login " + login + " not found"));
    }
}
