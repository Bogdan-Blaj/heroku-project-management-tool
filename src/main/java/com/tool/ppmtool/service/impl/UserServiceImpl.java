package com.tool.ppmtool.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tool.ppmtool.domain.User;
import com.tool.ppmtool.exception.user.UsernameAlreadyExistsException;
import com.tool.ppmtool.repository.UserRepository;
import com.tool.ppmtool.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	// password Encoder to not store readable data e.g. passwords
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder; // we need to add bean to autowire and use it

	@Override
	public User saveUser(User newUser) {
		 try{
             newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
             //Username has to be unique (exception)
             newUser.setUsername(newUser.getUsername());
             // Make sure that password and confirmPassword match
             // We don't persist or show the confirmPassword
             newUser.setConfirmPassword("");
             return userRepository.save(newUser);

         }catch (Exception e){
             throw new UsernameAlreadyExistsException("Username '"+newUser.getUsername()+"' already exists");
         }
		
	}
}
