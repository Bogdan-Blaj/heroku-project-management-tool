package com.tool.ppmtool.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.tool.ppmtool.service.impl.CustomUserDetailsServiceImpl;

import static com.tool.ppmtool.security.SecurityConstants.SIGN_UP_URLS;
import static com.tool.ppmtool.security.SecurityConstants.H2_URL;

import org.springframework.security.config.BeanIds;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
		securedEnabled = true,
		jsr250Enabled = true,
		prePostEnabled = true
		)
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private JwtAuthenticationEntryPoint unathorizedHandler;
	
	@Autowired
	private CustomUserDetailsServiceImpl customUserDetailsService; //can't use interfaces for service
	
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {return  new JwtAuthenticationFilter();}

	
	
	
	@Override
	protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		//we specify which user details service will use
		 authenticationManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(bCryptPasswordEncoder);
	}
	

	@Override
	@Bean(BeanIds.AUTHENTICATION_MANAGER) //to be able to autowire in the controller
	protected AuthenticationManager authenticationManager() throws Exception {
		// TODO Auto-generated method stub
		return super.authenticationManager();
	}




	@Override
	protected void configure(HttpSecurity http) throws Exception {

		//customise the authentication Entry Point, this handles the exception 
		//we want our security to be stateless, Redux will handle the state
	http.cors().and().csrf().disable()
		.exceptionHandling().authenticationEntryPoint(unathorizedHandler).and()
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		.headers().frameOptions().sameOrigin() //To enable H2 Database
		 .and()
	     .authorizeRequests()
	     .antMatchers(
                "/",
                "/favicon.ico",
                "/**/*.png",
                "/**/*.gif",
                "/**/*.svg",
                "/**/*.jpg",
                "/**/*.html",
                "/**/*.css",
                "/**/*.js"
        ).permitAll()
         .antMatchers(SIGN_UP_URLS).permitAll()
         .antMatchers(H2_URL).permitAll()
        .anyRequest().authenticated();
	
	 http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
}
}