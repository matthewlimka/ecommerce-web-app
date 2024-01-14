package com.matthewlim.ecommercewebapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.matthewlim.ecommercewebapp.services.UserService;

@RestController
@RequestMapping("api/v1/users")
public class UserController {
	
	@Autowired
	private UserService userService;
}
