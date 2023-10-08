package com.bezkoder.spring.entity.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChangePasswordData {
    private String passwordLama;
    @Size(min = 6, max = 40)
    private String passwordBaru;

    
    private String passwordBaruConfirmation;
    
}
