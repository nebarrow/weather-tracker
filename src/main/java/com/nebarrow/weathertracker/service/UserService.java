package com.nebarrow.weathertracker.service;

import com.nebarrow.weathertracker.dto.request.PostUserRequest;
import com.nebarrow.weathertracker.dto.response.GetUserResponse;
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
    public GetUserResponse create(PostUserRequest postUser) {
        Optional<GetUserResponse> existingUser = userRepository.findByLogin(postUser.login()).map(userMapper::toDto);
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("User already exists");
        }
        var user = userMapper.toUser(postUser);
        return userMapper.toDto(userRepository.save(user));
    }

    @Transactional
    public GetUserResponse findById(int id) {
        return userRepository.findById(id).map(userMapper::toDto).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Transactional
    public GetUserResponse findByLogin(String login) {
        return userRepository.findByLogin(login).map(userMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException("Invalid credentials"));
    }
}
