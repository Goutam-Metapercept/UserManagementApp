package com.lade.JWT;

import com.lade.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String jwtToken = null;

        // Step 1: Try to get JWT from Authorization header
        final String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwtToken = authHeader.substring(7);
        }

        // Step 2: If not in header, check for JWT cookie
        if (jwtToken == null) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("JWT".equals(cookie.getName())) {
                        jwtToken = cookie.getValue();
                        break;
                    }
                }
            }
        }

        // Step 3: If no token found, continue filter chain
        if (jwtToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // Step 4: Extract user ID from token
        String userId = jwtService.extractUserId(jwtToken);
        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            var userDetails = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (jwtService.isTokenValid(jwtToken, userDetails)) {
                List<SimpleGrantedAuthority> authorities = userDetails.getRoles().stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}


//package com.lade.JWT;
//
//import java.io.IOException;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import com.lade.repository.UserRepository;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//public class JWTAuthenticationFilter extends OncePerRequestFilter {
//
//	@Autowired 
//	private UserRepository userRepository;
//	@Autowired
//	private JwtService jwtService;
//	@Override
//	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//			throws ServletException, IOException {
//		// TODO Auto-generated method stub
//		
//		String username=null;
//		String jwtToken=null;
//		final String authHeader=request.getHeader("Authorize");
//		if(authHeader!=null && authHeader.startsWith("Bearer")) {
//			jwtToken=authHeader.substring(7);
//			
//		}
//		//if jwt token  is null, need to check the cookie
//		if(jwtToken==null) {
//			Cookie []cookies=request.getCookies();
//			if(cookies!=null) {
//				for(Cookie cookie:cookies) {
//					if("JWT".equals(cookie.getName())) {
//						jwtToken=cookie.getValue();
//						break;
//					}
//				}
//			}
//			
//		}
////if not token found		
//		if(jwtToken==null)
//		{
//			filterChain.doFilter(request, response);
//			return;
//		}
//	
////extract the userid from jwt token
//	username=jwtService.extractUsername(jwtToken);
//	if(userId!=null  && SecurityContextHolder.getContext().getAuthentication()==null) {
//		var userDetails=userRepository.findById(username)
//				.orElseThrow(()->new RuntimeException("User not found"));
//	}
//	
//}
//}