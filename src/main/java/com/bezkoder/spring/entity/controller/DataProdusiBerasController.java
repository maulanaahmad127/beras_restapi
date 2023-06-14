package com.bezkoder.spring.entity.controller;



import java.util.List;

import javax.validation.Valid;

import com.bezkoder.spring.entity.dto.DataProdusiBerasData;
import com.bezkoder.spring.entity.dto.ResponseData;
import com.bezkoder.spring.entity.model.DataBeras;
import com.bezkoder.spring.entity.model.DataPenjualanBeras;
import com.bezkoder.spring.entity.model.DataProdusiBeras;
import com.bezkoder.spring.entity.model.JenisBeras;
import com.bezkoder.spring.entity.repo.DataProdusiBerasRepo;
import com.bezkoder.spring.entity.repo.JenisBerasRepo;
import com.bezkoder.spring.entity.service.DataProdusiBerasService;
import com.bezkoder.spring.entity.util.GetUsernameToken;
import com.bezkoder.spring.login.models.User;
import com.bezkoder.spring.login.repository.UserRepository;

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
@RequestMapping("api/beras")
public class DataProdusiBerasController {
    
    @Autowired
    private DataProdusiBerasService service;

    @Autowired
    private DataProdusiBerasRepo repo;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JenisBerasRepo jenisBerasRepo;

    @Autowired
    private GetUsernameToken getUsername;
    

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseData<DataProdusiBeras>> create(@Valid @RequestBody DataProdusiBerasData berasData, Errors errors){
        ResponseData<DataProdusiBeras> responseData = new ResponseData<>();
        if(errors.hasErrors()){
            for (ObjectError error : errors.getAllErrors()) {
                responseData.getMessage().add(error.getDefaultMessage());
            }
            responseData.setStatus(false);
            responseData.setPayload(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }
        
        DataProdusiBeras beras = new DataProdusiBeras(
            berasData.getBerat_beras(),
            berasData.getHarga()
        );

        User petani = userRepository.findById(berasData.getPetani()).get();
        JenisBeras jb = jenisBerasRepo.findById(berasData.getJenisBeras()).get();

        beras.setPetani(petani);
        beras.setJenisBeras(jb);
        responseData.setStatus(true);
        responseData.setPayload(service.save(beras));
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('PETANI') or hasRole('ADMIN') or hasRole('PK')")
    public DataProdusiBeras findOne(@PathVariable("id") Long id){
        
        return service.findOne(id);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PK')")
    public Iterable<DataProdusiBeras> findAll(){
        return service.findAll();
    }

    @GetMapping("/petani")
    @PreAuthorize("hasRole('PETANI')")
    public Iterable<DataProdusiBeras> findByPetani(){
        User petani = userRepository.findByUsername(getUsername.getUsernameStringFromToken()).get();
        System.out.println(petani.getUsername());
        return service.findByPetani(petani);
    }

    @GetMapping("/getDataBeras")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PK')")
    public List<DataBeras> findAllDataBeras(){
        return repo.sumStokBeras();
    }

    @GetMapping("/getDataPenjualanBeras")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PK')")
    public List<DataPenjualanBeras> findAllDataPenjualanBeras(){
        return repo.dataPenjualanBeras();
    }

    @GetMapping("/getPetani")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PK')")
    public Iterable<User> findAllPetani(){
        return userRepository.findByRoles();
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseData<DataProdusiBeras>> update(@PathVariable("id") Long id, @Valid @RequestBody DataProdusiBerasData berasData, Errors errors){
        ResponseData<DataProdusiBeras> responseData = new ResponseData<>();
        if(errors.hasErrors()){
            for (ObjectError error : errors.getAllErrors()) {
                responseData.getMessage().add(error.getDefaultMessage());
            }
            responseData.setStatus(false);
            responseData.setPayload(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }
        
        DataProdusiBeras beras = service.findOne(id);
        beras.setBerat_beras(berasData.getBerat_beras());
        beras.setHarga(berasData.getHarga());

        User petani = userRepository.findById(berasData.getPetani()).get();
        JenisBeras jb = jenisBerasRepo.findById(berasData.getJenisBeras()).get();

        beras.setPetani(petani);
        beras.setJenisBeras(jb);
        responseData.setStatus(true);
        responseData.setPayload(service.save(beras));
        return ResponseEntity.ok(responseData);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable("id") Long id){
        service.removeOne(id);
    }

    @PutMapping("/changeIsTerjual/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void updateStatusIsTerjual(@PathVariable("id") Long id){
        service.updateStatusisTerjual(true, id);
    }

}

