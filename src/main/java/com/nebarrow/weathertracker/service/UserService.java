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
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    public void create(PostUser postUser) {
        if (findByLogin(postUser.login()).isEmpty()) {
            userRepository.save(userMapper.toUser(postUser));
        } else {
            throw new UserAlreadyExistsException("User with login " + postUser.login() + " already exists");
        }
    }

    public Optional<GetUser> findById(int id) {
        return userRepository.findById(id).map(userMapper::toDto);
    }

    public Optional<GetUser> findByLogin(String login) {
        return userRepository.findByLogin(login).map(userMapper::toDto);
    }
}
