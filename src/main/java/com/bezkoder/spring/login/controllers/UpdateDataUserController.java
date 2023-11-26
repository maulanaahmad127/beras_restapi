package com.bezkoder.spring.login.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.bezkoder.spring.entity.dto.ChangeEmailData;
import com.bezkoder.spring.entity.dto.ChangePasswordData;
import com.bezkoder.spring.entity.dto.ChangeUsernameData;
import com.bezkoder.spring.entity.dto.ResponseData;
import com.bezkoder.spring.entity.util.EmailService;
import com.bezkoder.spring.entity.util.GetUsernameToken;
import com.bezkoder.spring.login.models.ConfirmationToken;
import com.bezkoder.spring.login.models.User;
import com.bezkoder.spring.login.payload.response.MessageResponse;

import org.springframework.web.bind.annotation.RequestBody;

import com.bezkoder.spring.login.repository.ConfirmationTokenRepository;
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

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private EmailService emailService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PK') or hasRole('PETANI')")
    public User findUserDetail() {
        String userString = getUsername.getUsernameStringFromToken();
        User user = userRepository.findByUsername(userString).get();
        System.out.println(user.getPassword());
        return user;
    }

    @PatchMapping("/changeEmail")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PK') or hasRole('PETANI')")
    public ResponseEntity<?> changeEmail(@Valid @RequestBody ChangeEmailData changeEmailData, Errors errors,
            BindingResult bindingResult) {
        ResponseData<User> responseData = new ResponseData<>();
        if (errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                responseData.getMessage().add(error.getDefaultMessage());
            }
            responseData.setStatus(false);
            responseData.setPayload(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }
        if (bindingResult.hasErrors()) {
            List<FieldError> errorss = bindingResult.getFieldErrors();
            List<String> message = new ArrayList<>();
            for (FieldError e : errorss) {
                message.add("@" + e.getField().toUpperCase() + ":" + e.getDefaultMessage());
                return ResponseEntity.badRequest().body(new MessageResponse(message.toString()));
            }

        }

        String userString = getUsername.getUsernameStringFromToken();
        User user = userRepository.findByUsername(userString).get();

        if (changeEmailData.getEmail().isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email kosong!"));
        }

        if (userRepository.existsByEmail(changeEmailData.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email telah digunakan!"));
        }
        user.setEmail(changeEmailData.getEmail());
        user.setEmailActivated(false);
        ConfirmationToken confirmationToken = new ConfirmationToken(user);

        confirmationTokenRepository.save(confirmationToken);

        final String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

        System.out.println("base url = " + baseUrl);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setText("To confirm your account, please click here : "
                + baseUrl + "/api/user/verivEmail?token=" + confirmationToken.getConfirmationToken());

        emailService.sendEmail(mailMessage);
        userRepository.save(user);
        
        responseData.setStatus(true);
        responseData.getMessage().add("email verivikasi telah dikirimkan");
        responseData.setPayload(user);
        return ResponseEntity.ok(responseData);
    }

    @PatchMapping("/changeUsername")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PK') or hasRole('PETANI')")
    public ResponseEntity<?> changeUsername(@RequestBody @Valid ChangeUsernameData changeUsernameData, Errors errors,
            BindingResult bindingResult) {
        ResponseData<User> responseData = new ResponseData<>();
        if (errors.hasErrors()) {
            for (ObjectError error : errors.getAllErrors()) {
                responseData.getMessage().add(error.getDefaultMessage());
            }
            responseData.setStatus(false);
            responseData.setPayload(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }

        if (bindingResult.hasErrors()) {
            List<FieldError> errorss = bindingResult.getFieldErrors();
            List<String> message = new ArrayList<>();
            for (FieldError e : errorss) {
                message.add("@" + e.getField().toUpperCase() + ":" + e.getDefaultMessage());
                return ResponseEntity.badRequest().body(new MessageResponse(message.toString()));
            }

        }

        String userString = getUsername.getUsernameStringFromToken();
        User user = userRepository.findByUsername(userString).get();
        if (changeUsernameData.getUsername().isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username kosong!"));
        }
        if (userRepository.existsByUsername(changeUsernameData.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username telah digunakan!"));
        }
        user.setUsername(changeUsernameData.getUsername());

        responseData.setStatus(true);
        responseData.setPayload(userRepository.save(user));
        return ResponseEntity.ok(responseData);
    }

    @PatchMapping("/changePassword")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PK') or hasRole('PETANI')")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordData changePasswordData, Errors errors) {
        ResponseData<User> responseData = new ResponseData<>();
        if (errors.hasErrors()) {
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
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(userString, changePasswordData.getPasswordLama()))
                    .isAuthenticated();

            if (changePasswordData.getPasswordLama().isEmpty()) {
                return ResponseEntity.badRequest().body(new MessageResponse("Error: Password Lama kosong!"));
            }

            if (changePasswordData.getPasswordBaru().isEmpty()) {
                return ResponseEntity.badRequest().body(new MessageResponse("Error: Password Baru kosong!"));
            }

            if (changePasswordData.getPasswordBaruConfirmation().isEmpty()) {
                return ResponseEntity.badRequest().body(new MessageResponse("Error: Konfirmasi Password Baru kosong!"));
            }

            if (changePasswordData.getPasswordLama().equals(changePasswordData.getPasswordBaru())) {
                responseData.getMessage().add("password lama dan password baru sama");
                responseData.setStatus(false);
                responseData.setPayload(null);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
            }

            if (!changePasswordData.getPasswordBaru().equals(changePasswordData.getPasswordBaruConfirmation())) {
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("Error: konfirmasi password baru salah!"));
            }

            if (isAuthenticated == true) {
                System.out.println(isAuthenticated);
                String encodedPasswordBaru = passwordEncoder.encode(changePasswordData.getPasswordBaru());
                user.setPassword(encodedPasswordBaru);
            }

        } catch (Exception e) {
            responseData.getMessage().add(e.getLocalizedMessage());
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

    @GetMapping("/sendEmailVeriv")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PK') or hasRole('PETANI')")
    public ResponseEntity<?> sendEmailVerivication() {

        ResponseData<User> responseData = new ResponseData<>();

        String userString = getUsername.getUsernameStringFromToken();
        User user = userRepository.findByUsername(userString).get();

        ConfirmationToken confirmationToken = new ConfirmationToken(user);

        confirmationTokenRepository.save(confirmationToken);

        final String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();

        System.out.println("base url = " + baseUrl);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Complete Registration!");
        mailMessage.setText("To confirm your account, please click here : "
                + baseUrl + "/api/user/verivEmail?token=" + confirmationToken.getConfirmationToken());

        emailService.sendEmail(mailMessage);

        responseData.setStatus(true);
        responseData.getMessage().add("email verivikasi telah dikirimkan");
        responseData.setPayload(null);
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/verivEmail")
    public ResponseEntity<?> verivEmail(@RequestParam("token") String confirmationToken) {
        ResponseData<User> responseData = new ResponseData<>();
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
        if (token != null) {
            User user = userRepository.findByEmail(token.getUser().getEmail()).get();

            user.setEmailActivated(true);
            userRepository.save(user);
            responseData.setStatus(true);
            responseData.getMessage().add("email berhasil diverivikasi");
            responseData.setPayload(user);
            return ResponseEntity.ok("email berhasil diverivikasi");
        }
        responseData.setStatus(false);
        responseData.getMessage().add("token tidak valid");
        responseData.setPayload(null);
        return ResponseEntity.badRequest().body(null);
    }

}
