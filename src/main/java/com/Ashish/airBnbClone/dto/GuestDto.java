package com.Ashish.airBnbClone.dto;

import com.Ashish.airBnbClone.entity.User;
import com.Ashish.airBnbClone.entity.enums.Gender;
import lombok.Data;

@Data
public class GuestDto {
    private Long id;
    private User user;
    private String name;
    private Gender gender;
    private Integer age;
}

