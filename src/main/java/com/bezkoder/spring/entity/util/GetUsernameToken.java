package com.bezkoder.spring.entity.util;

import javax.servlet.http.HttpServletRequest;

import com.bezkoder.spring.login.models.User;
import com.bezkoder.spring.login.repository.UserRepository;
import com.bezkoder.spring.login.security.jwt.JwtUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GetUsernameToken {
    
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserRepository userRepository;

    public String getUsernameStringFromToken (){
        String token = jwtUtils.getJwtFromCookies(request);
        String username = jwtUtils.getUserNameFromJwtToken(token);
        return username;
    }

    public User getUserNameFromToken(){
        String username = getUsernameStringFromToken();
        User user = userRepository.findByUsername(username).get();
        return user;
    }

}
