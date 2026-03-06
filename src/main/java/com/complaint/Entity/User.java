package com.complaint.Entity;

import com.complaint.Dto.UserDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Getter
@Setter
public class User {
    private long id;
    private String name;
    private String email;
    private String password;
    private String role;
    private String userId;
    private LocalDateTime createdAt;
    private String tel;

    // dto -> entity
    public static User from(UserDto userDto,
                            PasswordEncoder passwordEncoder){
       User user = new User();
       user.setUserId(userDto.getUserId());
       user.setName(userDto.getName());
       user.setEmail(userDto.getEmail());
       user.setPassword(passwordEncoder.encode(userDto.getPassword()));
       user.setTel(userDto.getTel());
       user.setRole("USER");

       return user;
    }
}
