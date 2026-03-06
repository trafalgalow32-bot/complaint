package com.complaint.Dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {
    @NotEmpty(message = "아이디는 필수입니다.")
    private String userId;
    private String password;
}
