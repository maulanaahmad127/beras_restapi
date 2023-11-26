package com.bezkoder.spring.entity.service;

import java.util.Optional;

import javax.transaction.Transactional;

import com.bezkoder.spring.entity.model.JenisBeras;
import com.bezkoder.spring.entity.repo.JenisBerasRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class JenisBerasService {
    
    @Autowired
    private JenisBerasRepo jenisBerasRepo;

    public JenisBeras save(JenisBeras beras){
        return jenisBerasRepo.save(beras);
    }

    public JenisBeras findOne(Long id){
            Optional<JenisBeras> beras = jenisBerasRepo.findById(id);
            if(!beras.isPresent()){
                return null;
            }
            return beras.get();
    }
    
    public Iterable<JenisBeras> findAll(){
        return jenisBerasRepo.findAll();
    }

    public Iterable<JenisBeras> findByNameContains(String nama, Pageable pageable){
        return jenisBerasRepo.findByNamaContains(nama, pageable);
    }

    public void removeOne(Long id){
        jenisBerasRepo.deleteById(id);
    }

}

