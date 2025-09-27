package com.example.BookMyShow.Service;

import com.example.BookMyShow.DTO.UserDTO;
import com.example.BookMyShow.Exception.ResourceNotFound;
import com.example.BookMyShow.Model.User;
import com.example.BookMyShow.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserDTO createUser(UserDTO userDTO){
        User user = mapToEntity(userDTO);

        User savedUser = userRepository.save(user);
        return mapToDto(savedUser);
    }

    public UserDTO getUserById(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("User not found with id " + id));

        return mapToDto(user);
    }

    public List<UserDTO> getAllUsers(){
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // Update User
    public UserDTO updateUser(UserDTO newUser, Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("User not found with id " + id));

        user.setName(newUser.getName());
        user.setEmail(newUser.getEmail());
        user.setPhoneNumber(newUser.getPhoneNumber());

        User savedUser = userRepository.save(user);
        return mapToDto(savedUser);
    }

    public void deleteUser(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("User not found with id " + id));

        userRepository.delete(user);
//        return mapToDto(user);
    }

    private UserDTO mapToDto(User user){
        UserDTO userDTO = new UserDTO();

        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhoneNumber(user.getPhoneNumber());

        return userDTO;
    }

    private User mapToEntity(UserDTO userDTO){
        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPhoneNumber(userDTO.getPhoneNumber());

        return user;
    }

}
