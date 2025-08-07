package com.Group6.WebAppDevGroupProject.Repository;

import com.Group6.WebAppDevGroupProject.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> { }
