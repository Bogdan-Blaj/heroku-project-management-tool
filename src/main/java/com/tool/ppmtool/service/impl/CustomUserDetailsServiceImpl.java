package com.tool.ppmtool.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tool.ppmtool.domain.User;
import com.tool.ppmtool.repository.UserRepository;

@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);

		if (user == null)
			throw new UsernameNotFoundException("User not found");
		
		return user;
	}
	
	//for future use
	@Transactional
	public User loadUserById(Long id){
		User user = userRepository.getById(id);
		
		if (user == null)
			throw new UsernameNotFoundException("User not found");
		
		return user;
	}

}
