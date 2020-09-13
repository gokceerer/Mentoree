package com.gokceerer.Mentoree.Security;

import com.gokceerer.Mentoree.Repositories.SQL.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Configuration
public class CustomLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Autowired
    private UserRepository userRepository;

    @Override
    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String targetUrl= determineTargetUrl(authentication);
        if(response.isCommitted()){
            return;
        }
        RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
        redirectStrategy.sendRedirect(request,response,targetUrl);
    }

    protected String determineTargetUrl(Authentication authentication){
        String url="/login?error=true";
        UserDetails details = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(details.getUsername().equals("mentoree.master@gmail.com")){
            url="/dashboard/admin";
        }
        else{
            if(userRepository.existsByUsername(details.getUsername())){
                url = "/dashboard/user";
            }
        }
        return url;
    }

}
