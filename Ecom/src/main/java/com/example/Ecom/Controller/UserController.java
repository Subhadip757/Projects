package com.example.Ecom.Controller;

import com.example.Ecom.Model.User;
import com.example.Ecom.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin("*")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public User registerUser(@RequestBody User user){
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public User LoginUser(@RequestBody User user){
        return userService.LoginUser(user.getEmail(), user.getPassword());
    }

    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }
}
