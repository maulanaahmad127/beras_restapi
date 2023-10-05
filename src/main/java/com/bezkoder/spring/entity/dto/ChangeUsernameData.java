package com.bezkoder.spring.entity.dto;

import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChangeUsernameData {
    
    @Size(min = 3, max = 20)
    private String username;
}
