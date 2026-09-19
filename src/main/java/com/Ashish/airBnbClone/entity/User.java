package com.Ashish.airBnbClone.entity;


import com.Ashish.airBnbClone.entity.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Table(name = "app_user") // Remember we can't make an entity of type "User" in postgresql.
public class User implements UserDetails { // We need to implement it so that we can use Spring security inbuild methods like Authentication-Manager. Because those things will look for UserDetails only.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String name;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_"+role.name()))
                .collect(Collectors.toSet());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(!(o instanceof User user)) return false;
//        Here Java does two things:
//        Checks whether o is an instance of User.
//        If it is, automatically casts o to User and stores it in the variable user.

        return Objects.equals(getId(), user.getId());
    }

    @Override
    public int hashCode(){
        return Objects.hashCode(getId());
    }
}
