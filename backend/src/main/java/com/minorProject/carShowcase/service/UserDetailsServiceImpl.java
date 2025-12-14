package com.minorProject.carShowcase.service;

import com.minorProject.carShowcase.model.User;
import com.minorProject.carShowcase.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * This service is used by Spring Security to load a user by their username
 * during the login (authentication) process.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Spring Security calls this method when a user tries to log in.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // We find the user in our database by their username.
        // The User model already implements UserDetails, so we can just return it.
        User userDetails = userRepository.findByUsername(username);
        if(userDetails!=null){
            return userDetails;
        }else{
            throw  new UsernameNotFoundException("User not found with username: " + username);
        }
    }
}