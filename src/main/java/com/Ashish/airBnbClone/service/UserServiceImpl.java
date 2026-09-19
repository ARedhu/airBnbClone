package com.Ashish.airBnbClone.service;

import com.Ashish.airBnbClone.entity.User;
import com.Ashish.airBnbClone.exception.ResourceNotFoundException;
import com.Ashish.airBnbClone.repository.UserRespository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRespository userRespository;

    @Override
    public User getUserById(Long id) {
        return userRespository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: "+id));
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRespository.findByEmail(username).orElse(null);
    }
}

