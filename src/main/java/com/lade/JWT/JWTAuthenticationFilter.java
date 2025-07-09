package com.lade.JWT;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtService jwtService;
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		Long userId=null;
		String jwtToken=null;
		final String authHeader=request.getHeader("Authorize");
		if(authHeader!=null && authHeader.startsWith("Bearer")) {
			jwtToken=authHeader.substring(7);
			
		}
		//if jwt token  is null, need to check the cookie
		if(jwtToken==null) {
			Cookie []cookies=request.getCookies();
			if(cookies!=null) {
				for(Cookie cookie:cookies) {
					if("JWT".equals(cookie.getName())) {
						jwtToken=cookie.getValue();
						break;
					}
				}
			}
			
		}
//if not token found		
		if(jwtToken==null)
		{
			filterChain.doFilter(request, response);
		}
	}

}
