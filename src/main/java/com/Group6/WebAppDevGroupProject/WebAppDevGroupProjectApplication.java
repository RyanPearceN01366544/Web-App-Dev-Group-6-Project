package com.Group6.WebAppDevGroupProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.annotation.PostConstruct;
import com.Group6.WebAppDevGroupProject.Service.MenuService;

@SpringBootApplication
 public class WebAppDevGroupProjectApplication {

	@Autowired
	private MenuService menuService;

	@PostConstruct
	public void seedMenu() {
		menuService.seedMenuItems();
	}

	public static void main(String[] args) {
		SpringApplication.run(WebAppDevGroupProjectApplication.class, args);
	}

}