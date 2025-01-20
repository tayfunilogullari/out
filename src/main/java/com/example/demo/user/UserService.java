package com.example.demo.user;

import com.example.demo.user.dto.UserDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public interface UserService {

    UserDTO getUser(Long id);

    UserDTO getUserByName(String name);

    List<UserDTO> getAllUsers();

    @Transactional
    UserDTO addUser(UserDTO userDto);

    @Transactional
    UserDTO updateUser(UserDTO userDto, Long userId);

    @Transactional
    boolean deleteUser(Long id);

}
