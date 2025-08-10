package com.Group6.WebAppDevGroupProject.Service;

import com.Group6.WebAppDevGroupProject.Models.User;
import com.Group6.WebAppDevGroupProject.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class UserService {
    @Autowired
    private UserRepository userRepo;

    public Optional<User> getByID(long id) {
        return userRepo.findById(id);
    }
}
