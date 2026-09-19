package com.Ashish.airBnbClone.security;

import com.Ashish.airBnbClone.dto.LoginReqDto;
import com.Ashish.airBnbClone.dto.SignupReqDto;
import com.Ashish.airBnbClone.dto.UserDto;
import com.Ashish.airBnbClone.entity.User;
import com.Ashish.airBnbClone.entity.enums.Role;
import com.Ashish.airBnbClone.exception.ResourceNotFoundException;
import com.Ashish.airBnbClone.repository.UserRespository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRespository userRespository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    public UserDto signup(SignupReqDto signupReqDto){
        User user = userRespository.findByEmail(signupReqDto.getEmail()).orElse(null);
        if(user != null){
            throw  new RuntimeException("User is already present with same email id");
        }

        User newUser = modelMapper.map(signupReqDto, User.class);
        newUser.setRoles(Set.of(Role.GUEST));
        newUser.setPassword(passwordEncoder.encode(signupReqDto.getPassword()));
        newUser = userRespository.save(newUser);

        return modelMapper.map(newUser, UserDto.class);
    }

    public String[] login(LoginReqDto loginReqDto){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginReqDto.getEmail(), loginReqDto.getPassword())
        );
        // If user not authenticate, the upper code will throw an exception.

        User user = (User) authentication.getPrincipal();

        String[] arr = new String[2];
        arr[0] = jwtService.generateAccessToken(user);
        arr[1] = jwtService.generateRefreshToken(user);

        return arr;
    }

    public String refreshToken(String refreshToken){
        Long id = jwtService.getUserIdFromToken(refreshToken);

        User user = userRespository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: "+id));
        return jwtService.generateAccessToken(user);
    }
}
