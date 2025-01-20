package com.example.demo.user;

import com.example.demo.user.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/users")
public class UserController {

    private final UserService userService;

    @GetMapping (value = "/hello")
    @ResponseBody
    public String healthCheck() {
        return "Hello";
    }


    @GetMapping (value = "/kullanici/{id}")
    @ResponseBody
    public UserDTO getById(@PathVariable("id") Long userId) {
        return userService.getUser(userId);
    }

    @GetMapping(value = "/kullaniciadi")
    @ResponseBody
    public UserDTO getByName(@RequestParam("name") String name) {
        return userService.getUserByName(name);
    }

    @GetMapping ("/kullanicilar")
    @ResponseBody
    public List<UserDTO> getAll() {
        return userService.getAllUsers();
    }

    @PostMapping("/kullanici_ekle")
    public UserDTO saveUser(@RequestBody UserDTO userDto)
    {
        return userService.addUser(userDto);
    }

    @PutMapping("/guncelle/{id}")
    public UserDTO updateUser(@RequestBody UserDTO userDto,
                              @PathVariable("id") Long userId)
    {
        return userService.updateUser(userDto, userId);
    }

    @DeleteMapping("/sil/{id}")
    public String deleteUserById(@PathVariable("id") Long userId)
    {
        userService.deleteUser(userId);
        return "Deleted Successfully";
    }
}
