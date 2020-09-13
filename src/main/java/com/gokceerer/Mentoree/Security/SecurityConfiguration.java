package com.gokceerer.Mentoree.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private CustomLoginSuccessHandler customLoginSuccessHandler;
    @Autowired
    private CustomGoogleLoginSuccessHandler customGoogleLoginSuccessHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                    .antMatchers("/resources/**").permitAll()
                    .antMatchers("/", "/login").permitAll()
                    .antMatchers("/dashboard/admin").hasRole("ADMIN")
                    .antMatchers("/editTopics").hasRole("ADMIN")
                    .antMatchers("/dashboard/user").hasRole("USER")
                    .antMatchers("/mentorApplication").hasRole("USER")
                    .antMatchers("/mentorSearch").hasRole("USER")
                    .antMatchers("/mentorshipDetail/**").hasRole("USER")
                    .anyRequest().authenticated()
                    .and()
                .formLogin()
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
                    .successHandler(customLoginSuccessHandler)
                    .permitAll()
                    .and()
                .oauth2Login()
                    .loginPage("/login")
                    .successHandler(customGoogleLoginSuccessHandler)
                .and()
                .logout();




    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .ldapAuthentication()
                .userDnPatterns("uid={0},ou=people")
                .groupSearchBase("ou=roles")
                .groupSearchFilter("uniqueMember={0}")
                .contextSource()
                .url("ldap://localhost:8389/dc=mentoree,dc=org")
                .and()
                .passwordCompare()
                .passwordEncoder(new BCryptPasswordEncoder())
                .passwordAttribute("userPassword");
    }

}
