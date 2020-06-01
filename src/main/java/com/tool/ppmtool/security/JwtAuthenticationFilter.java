package com.tool.ppmtool.security;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.tool.ppmtool.domain.User;
import com.tool.ppmtool.service.impl.CustomUserDetailsServiceImpl;

public class JwtAuthenticationFilter extends OncePerRequestFilter{
	//gives access to user to the resources if it can pass the filter
	
	@Autowired
	//to validate the token and extract the userID
	private JwtTokenProvider tokenProvider;
	
	@Autowired
	private CustomUserDetailsServiceImpl customUserDetailsServiceImpl;
	
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		try {
			String jwt = getJWTFromRequest(request);
			
			//check if token is valid
			if(StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
				Long userId = tokenProvider.getUserIdFromJWT(jwt);
                User userDetails = customUserDetailsServiceImpl.loadUserById(userId);
                
                //setup the authentication
                //by passing the token, no credentials
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, Collections.emptyList());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                
			}
			
			
		} catch (Exception ex) {
			logger.error("Could not set user authentication in security context", ex);
		}
		

        filterChain.doFilter(request, response);

	}

	private String getJWTFromRequest(HttpServletRequest request) {
		//get the header from the request
		String bearerToken = request.getHeader(SecurityConstants.HEADER_STRING);
		
		if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(SecurityConstants.TOKEN_PREFIX)) {
			return bearerToken.substring(7, bearerToken.length()); //return the rest of the header which is the token
		}
		
		return null;
	}
}
