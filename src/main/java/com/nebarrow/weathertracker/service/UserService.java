package com.nebarrow.weathertracker.service;

import com.nebarrow.weathertracker.dto.request.PostUser;
import com.nebarrow.weathertracker.dto.response.GetUser;
import com.nebarrow.weathertracker.exception.UserNotFoundException;
import com.nebarrow.weathertracker.mapper.UserMapper;
import com.nebarrow.weathertracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    public void create(PostUser postUser) {
        userRepository.save(userMapper.toUser(postUser));
    }

    public GetUser findById(int id) {
        return userRepository.findById(id).map(userMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException("User with id not found"));
    }
}
