package com.Ashish.airBnbClone.dto;

import lombok.Data;

@Data
public class SignupReqDto {
    private String email;
    private String password;
    private String name;
}
