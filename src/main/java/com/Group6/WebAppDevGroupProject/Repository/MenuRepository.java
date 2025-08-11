package com.Group6.WebAppDevGroupProject.Repository;

import com.Group6.WebAppDevGroupProject.Models.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<MenuItem, Long> { }