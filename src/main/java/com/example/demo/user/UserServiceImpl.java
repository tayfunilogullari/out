package com.example.demo.user;

import com.example.demo.db.User;
import com.example.demo.db.repo.UserRepo;
import com.example.demo.user.dto.UserDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserDTO getUser(Long id) {
        Optional<User> userOPT = userRepo.findById(id);
        User user = userOPT.orElseThrow();
        UserDTO dto = modelMapper.map(user, UserDTO.class);
        return dto;
    }

    @Override
    public UserDTO getUserByName(String name) {
        User user = userRepo.getUserByName(name);
        UserDTO dto = modelMapper.map(user, UserDTO.class);
        return dto;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepo.findAll();
        List<UserDTO> dtoList = new ArrayList<>();
        for (User user : users) {
            UserDTO dto = modelMapper.map(user, UserDTO.class);
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Override
    public UserDTO addUser(UserDTO userDto) {
        User user = modelMapper.map(userDto, User.class);
        user = userRepo.save(user);
        userDto = modelMapper.map(user, UserDTO.class);
        return userDto;
    }

    @Override
    public UserDTO updateUser(UserDTO userDto, Long userId) {
        User user = userRepo.getReferenceById(userId);

        user.setName(userDto.getName());
        user.setSurname(userDto.getSurname());
        user.setCreditLimit(userDto.getCreditLimit());
        user.setUsedCreditLimit(userDto.getUsedCreditLimit());

        user = userRepo.save(user);
        userDto = modelMapper.map(user, UserDTO.class);

        return userDto;
    }

    @Override
    public boolean deleteUser(Long id) {
        try {
            userRepo.deleteById(id);
        }
        catch (Exception ex) {
            return false;
        }
        return true;
    }

}
