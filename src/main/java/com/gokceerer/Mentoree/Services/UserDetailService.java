package com.gokceerer.Mentoree.Services;

import com.gokceerer.Mentoree.Models.SQL.User;
import com.gokceerer.Mentoree.Repositories.SQL.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService {
    private final UserRepository userRepository;

    public UserDetailService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User getLoggedInUser() {
        if(SecurityContextHolder.getContext().getAuthentication() instanceof OAuth2AuthenticationToken){
            OAuth2User details = ((OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication()).getPrincipal();
            return userRepository.findByUsername((String)details.getAttributes().get("email"));
        }
        else{
            UserDetails details = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return userRepository.findByUsername(details.getUsername());
        }

    }
}
