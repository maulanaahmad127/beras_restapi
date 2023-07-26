package com.bezkoder.spring.entity.controller;



import javax.validation.Valid;

import com.bezkoder.spring.entity.dto.JenisBerasData;
import com.bezkoder.spring.entity.dto.ResponseData;
import com.bezkoder.spring.entity.model.JenisBeras;
import com.bezkoder.spring.entity.service.JenisBerasService;
import com.bezkoder.spring.entity.util.GetUsernameToken;
import com.bezkoder.spring.login.models.User;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/jenisBeras")
public class JenisBerasController {
    
    @Autowired
    private JenisBerasService service;
    @Autowired
    private GetUsernameToken getUsername;

    

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseData<JenisBeras>> create(@Valid @RequestBody JenisBerasData jenisBerasData, Errors errors){
        ResponseData<JenisBeras> responseData = new ResponseData<>();
        if(errors.hasErrors()){
            for (ObjectError error : errors.getAllErrors()) {
                responseData.getMessage().add(error.getDefaultMessage());
            }
            responseData.setStatus(false);
            responseData.setPayload(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }
        
        JenisBeras jenisBeras = new JenisBeras(
            jenisBerasData.getNama()
        );

        
        responseData.setStatus(true);
        responseData.setPayload(service.save(jenisBeras));
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/{id}")
    @PreAuthorize(" hasRole('ADMIN') or hasRole('PK')")
    public JenisBeras findOne(@PathVariable("id") Long id){
        return service.findOne(id);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PK')")
    public Iterable<JenisBeras> findAll(){
        User user = getUsername.getUserNameFromToken();
        String username = user.getUsername();
        String role = user.getRoles().iterator().next().toString();
        System.out.printf("username: %s\nrole: %s", username, role);
        return service.findAll();
    }



    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseData<JenisBeras>> update( @Valid @RequestBody JenisBeras beras, Errors errors){
        ResponseData<JenisBeras> responseData = new ResponseData<>();
        if(errors.hasErrors()){
            for (ObjectError error : errors.getAllErrors()) {
                responseData.getMessage().add(error.getDefaultMessage());
            }
            responseData.setStatus(false);
            responseData.setPayload(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }
        responseData.setStatus(true);
        responseData.setPayload(service.save(beras));
        return ResponseEntity.ok(responseData);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable("id") Long id){
        service.removeOne(id);
    }

}

