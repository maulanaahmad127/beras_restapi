package com.bezkoder.spring.entity.controller;



import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.bezkoder.spring.entity.dto.DataProduksiBerasData;
import com.bezkoder.spring.entity.dto.ResponseData;
import com.bezkoder.spring.entity.model.DataBeras;
import com.bezkoder.spring.entity.model.DataPenjualanBeras;
import com.bezkoder.spring.entity.model.DataProduksiBeras;
import com.bezkoder.spring.entity.model.JenisBeras;
import com.bezkoder.spring.entity.model.PenjualanBeras;
import com.bezkoder.spring.entity.repo.DataProduksiBerasRepo;
import com.bezkoder.spring.entity.repo.JenisBerasRepo;
import com.bezkoder.spring.entity.service.DataProduksiBerasService;
import com.bezkoder.spring.entity.service.PenjualanBerasService;
import com.bezkoder.spring.entity.util.GetUsernameToken;
import com.bezkoder.spring.login.models.User;
import com.bezkoder.spring.login.repository.UserRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/beras")
public class DataProduksiBerasController {
    
    @Autowired
    private DataProduksiBerasService service;

    @Autowired
    private DataProduksiBerasRepo repo;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JenisBerasRepo jenisBerasRepo;

    @Autowired
    private GetUsernameToken getUsername;

    @Autowired
    private PenjualanBerasService penjualanService;
    

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseData<DataProduksiBeras>> create(@Valid @RequestBody DataProduksiBerasData berasData, Errors errors){
        ResponseData<DataProduksiBeras> responseData = new ResponseData<>();
        if(errors.hasErrors()){
            for (ObjectError error : errors.getAllErrors()) {
                responseData.getMessage().add(error.getDefaultMessage());
            }
            responseData.setStatus(false);
            responseData.setPayload(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }
        
        DataProduksiBeras beras = new DataProduksiBeras(
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
    public DataProduksiBeras findOne(@PathVariable("id") Long id){
        
        return service.findOne(id);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('PK')")
    public Iterable<DataProduksiBeras> findAll(){
        return service.findAll();
    }

    @GetMapping("/petani")
    @PreAuthorize("hasRole('PETANI')")
    public Iterable<DataProduksiBeras> findByPetani(){
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
    public ResponseEntity<ResponseData<DataProduksiBeras>> update(@PathVariable("id") Long id, @Valid @RequestBody DataProduksiBerasData berasData, Errors errors){
        ResponseData<DataProduksiBeras> responseData = new ResponseData<>();
        if(errors.hasErrors()){
            for (ObjectError error : errors.getAllErrors()) {
                responseData.getMessage().add(error.getDefaultMessage());
            }
            responseData.setStatus(false);
            responseData.setPayload(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }
        
        DataProduksiBeras beras = service.findOne(id);
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

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResponseData<DataProduksiBeras>> updatePartial(@PathVariable("id") Long id, @RequestBody Map<Object, Object> fields, Errors errors){
        ResponseData<DataProduksiBeras> responseData = new ResponseData<>();
        if(errors.hasErrors()){
            for (ObjectError error : errors.getAllErrors()) {
                responseData.getMessage().add(error.getDefaultMessage());
            }
            responseData.setStatus(false);
            responseData.setPayload(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }
        
        DataProduksiBeras beras = service.findOne(id);
        fields.forEach((key, value) -> {

            if(key.toString() == "petani"){
                Field field = ReflectionUtils.findField(DataProduksiBeras.class, (String) key);
                field.setAccessible(true);
                User petani = userRepository.findById( Long.parseLong(value.toString())).get();
                ReflectionUtils.setField(field, beras, petani);
            }else if(key.toString() == "jenisBeras"){
                Field field = ReflectionUtils.findField(DataProduksiBeras.class, (String) key);
                field.setAccessible(true);
                JenisBeras jenisBeras = jenisBerasRepo.findById( Long.parseLong(value.toString())).get();
                ReflectionUtils.setField(field, beras, jenisBeras);
            }
            else{
                Field field = ReflectionUtils.findField(DataProduksiBeras.class, (String) key);
                field.setAccessible(true);
                ReflectionUtils.setField(field, beras, value);
            }

            
        });
        
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
    public ResponseEntity<ResponseData<DataProduksiBeras>> updateStatusIsTerjual(@PathVariable("id") Long id){
        ResponseData<DataProduksiBeras> responseData = new ResponseData<>();
        DataProduksiBeras beras = service.findOne(id);

        if(beras.isTerjual() == true) {
            responseData.getMessage().add("beras sudah di set ke status terjual");
            responseData.setStatus(false);
            responseData.setPayload(null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseData);
        }

        beras.setTerjual(true);

        
        
        PenjualanBeras penjualanBeras = new PenjualanBeras();
        
        penjualanBeras.setBeras(beras);
        penjualanService.save(penjualanBeras);

        beras.setPenjualanBeras(penjualanBeras);

        service.save(beras);

        responseData.setPayload(beras);
        responseData.setStatus(true);

        return ResponseEntity.ok(responseData);
    }

}

