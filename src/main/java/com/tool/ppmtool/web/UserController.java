package com.tool.ppmtool.web;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tool.ppmtool.domain.User;
import com.tool.ppmtool.model.request.LoginRequest;
import com.tool.ppmtool.model.response.JWTLoginSucessReponse;
import com.tool.ppmtool.model.response.UserResponse;
import com.tool.ppmtool.security.JwtTokenProvider;
import com.tool.ppmtool.security.SecurityConstants;
import com.tool.ppmtool.service.UserService;
import com.tool.ppmtool.service.ValidationService;
import com.tool.ppmtool.validator.UserValidator;

@Controller
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ValidationService validationService;
	
	@Autowired
    private UserValidator userValidator;
	
    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;
	
	
	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, BindingResult result){
		ResponseEntity<?> errorMap = validationService.checkValid(result);
        if(errorMap != null)
        	return errorMap;
        
        //authenticate the user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        
        //here we are asuming we are authenticated
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        //generate Token
        String jwt = SecurityConstants.TOKEN_PREFIX + tokenProvider.generateToken(authentication);
        
        //return the token if the user is valid
		return ResponseEntity.ok(new JWTLoginSucessReponse(true, jwt));
	}
	
	 @PostMapping("/register")
     public ResponseEntity<?> registerUser(@Valid @RequestBody User user, BindingResult result){
		 
		//Validate Password match
		 userValidator.validate(user,result);
		 
		 
		//validate User fields
		ResponseEntity<?> errorMap = validationService.checkValid(result);
        if(errorMap != null)
        	return errorMap;
		
		 User newUser = userService.saveUser(user);
		 ModelMapper mapper = new ModelMapper();
		 UserResponse returnUser = mapper.map(newUser, UserResponse.class);
		 
         return  new ResponseEntity<UserResponse>(returnUser, HttpStatus.CREATED);
	}

}
