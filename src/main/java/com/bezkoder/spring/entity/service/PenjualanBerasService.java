package com.bezkoder.spring.entity.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bezkoder.spring.entity.model.PenjualanBeras;
import com.bezkoder.spring.entity.repo.PenjualanBerasRepo;

@Service
@Transactional
public class PenjualanBerasService {
    
    @Autowired
    private PenjualanBerasRepo repo;

    public PenjualanBeras save(PenjualanBeras beras){
        return repo.save(beras);
    }

    public PenjualanBeras findOne(Long id){
            Optional<PenjualanBeras> beras = repo.findById(id);
            if(!beras.isPresent()){
                return null;
            }
            return beras.get();
    }
    
    public Iterable<PenjualanBeras> findAll(){
        return repo.findAll();
    }

    public void removeOne(Long id){
        repo.deleteById(id);
    }
}
