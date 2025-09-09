package com.example.BookMyShow.Repository;

import com.example.BookMyShow.Model.Theatre;
import com.example.BookMyShow.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findByEmail(String email);
    boolean existingByEmail(String email);
}
