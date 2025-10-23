package com.example.Ecom.Service;

import com.example.Ecom.Model.User;
import com.example.Ecom.Repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepository;

    public User registerUser(User user){
        User newUser = userRepository.save(user);
        System.out.println("New User Added");
        return newUser;
    }

    public User LoginUser(String email, String password){
        User user = userRepository.findByEmail(email);
        if(user != null && user.getPassword().equals(password)){
            return user;
        }
        return null;
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

}
