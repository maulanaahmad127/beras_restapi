package com.bezkoder.spring.entity.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChangeEmailData {
    
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;
}
