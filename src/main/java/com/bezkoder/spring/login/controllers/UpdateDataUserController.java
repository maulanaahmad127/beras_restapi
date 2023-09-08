package com.bezkoder.spring.login.controllers;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bezkoder.spring.entity.dto.ChangeEmailData;
import com.bezkoder.spring.entity.dto.ChangePasswordData;
import com.bezkoder.spring.entity.dto.ChangeUsernameData;
import com.bezkoder.spring.entity.dto.ResponseData;
import com.bezkoder.spring.entity.util.GetUsernameToken;
import com.bezkoder.spring.login.models.User;
import com.bezkoder.spring.login.payload.response.MessageResponse;

import org.springframework.web.bind.annotation.RequestBody;
import com.bezkoder.spring.login.repository.UserRepository;



@RestController
@RequestMapping("/api/user")
public class UpdateDataUserController {
    
    @Autowired
    private GetUsernameToken getUsername;

    @Autowired
  AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PK') or hasRole('PETANI')")
    public User findUserDetail(){
        String userString = getUsername.getUsernameStringFromToken();
        User user = userRepository.findByUsername(userString).get();
        System.out.println(user.getPassword());
        return user;
    }

    @PatchMapping("/changeEmail")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PK') or hasRole('PETANI')")
    public ResponseEntity<?> changeEmailBro( @RequestBody ChangeEmailData changeEmailData, Errors errors){
        ResponseData<User> responseData = new ResponseData<>();
        if(errors.hasErrors()){
            for (ObjectError error : errors.getAllErrors()) {
                responseData.getMessage().add(error.getDefaultMessage());
            }
            responseData.setStatus(false);
            responseData.setPayload(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }
        
        String userString = getUsername.getUsernameStringFromToken();
        User user = userRepository.findByUsername(userString).get();
       
        
        if(changeEmailData.getEmail().isEmpty()){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is empty!"));
        }

        

        

        if (userRepository.existsByEmail(changeEmailData.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
          }
        user.setEmail(changeEmailData.getEmail());
        
        
        responseData.setStatus(true);
        responseData.setPayload(userRepository.save(user));
        return ResponseEntity.ok(responseData);
    }

    @PatchMapping("/changeUsername")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PK') or hasRole('PETANI')")
    public ResponseEntity<?> changeUsername(@RequestBody ChangeUsernameData changeUsernameData, Errors errors){
        ResponseData<User> responseData = new ResponseData<>();
        if(errors.hasErrors()){
            for (ObjectError error : errors.getAllErrors()) {
                responseData.getMessage().add(error.getDefaultMessage());
            }
            responseData.setStatus(false);
            responseData.setPayload(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }
        
        String userString = getUsername.getUsernameStringFromToken();
        User user = userRepository.findByUsername(userString).get();
        if(changeUsernameData.getUsername().isEmpty()){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is empty!"));
        }
        if (userRepository.existsByUsername(changeUsernameData.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
          }
        user.setUsername(changeUsernameData.getUsername());
        
        
        responseData.setStatus(true);
        responseData.setPayload(userRepository.save(user));
        return ResponseEntity.ok(responseData);
    }

    @PatchMapping("/changePassword")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PK') or hasRole('PETANI')")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordData changePasswordData, Errors errors){
        ResponseData<User> responseData = new ResponseData<>();
        if(errors.hasErrors()){
            for (ObjectError error : errors.getAllErrors()) {
                responseData.getMessage().add(error.getDefaultMessage());
            }
            responseData.setStatus(false);
            responseData.setPayload(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }
        
        String userString = getUsername.getUsernameStringFromToken();
        User user = userRepository.findByUsername(userString).get();
        
       
        try {
            boolean isAuthenticated = authenticationManager
            .authenticate(new UsernamePasswordAuthenticationToken(userString, changePasswordData.getPasswordLama())).isAuthenticated();
            
            if(changePasswordData.getPasswordLama().isEmpty()){
                return ResponseEntity.badRequest().body(new MessageResponse("Error: Password Lama is empty!"));
            }

            if(changePasswordData.getPasswordBaru().isEmpty()){
                return ResponseEntity.badRequest().body(new MessageResponse("Error: Password Baru is empty!"));
            }
            
            if(changePasswordData.getPasswordLama().equals(changePasswordData.getPasswordBaru())){
                responseData.getMessage().add("password lama dan password baru sama");
                responseData.setStatus(false);
                responseData.setPayload(null);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
            }
            
            if(isAuthenticated == true){
                System.out.println(isAuthenticated);
                String encodedPasswordBaru = passwordEncoder.encode(changePasswordData.getPasswordBaru());
                user.setPassword(encodedPasswordBaru);
            }
            
        } catch (Exception e) {
            responseData.getMessage().add("password lama salah");
                responseData.setStatus(false);
                responseData.setPayload(null);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }

        
        responseData.setStatus(true);
        userRepository.save(user);
        responseData.getMessage().add("password berhasil diganti");
        responseData.setPayload(null);
        return ResponseEntity.ok(responseData);
    }
    


}
