package com.gokceerer.Mentoree.Security;

import com.gokceerer.Mentoree.Repositories.SQL.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class CustomGoogleLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Autowired
    private UserRepository userRepository;

    @Override
    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String targetUrl= determineTargetUrl((OAuth2AuthenticationToken) authentication);
        if(response.isCommitted()){
            return;
        }
        RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
        redirectStrategy.sendRedirect(request,response,targetUrl);
    }

    protected String determineTargetUrl(OAuth2AuthenticationToken authentication){
        String url="/login?error=true";
        OAuth2User userDetails = authentication.getPrincipal();
        if(userDetails.getAttributes().get("email").equals("mentoree.master@gmail.com")){
            url="/dashboard/admin";
        }
        else{
            if(userRepository.existsByUsername((String)userDetails.getAttributes().get("email"))){
                url = "/dashboard/user";
            }
        }
        return url;
    }
}
