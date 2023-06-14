package com.bezkoder.spring.entity.service;

import java.util.Optional;

import javax.transaction.Transactional;

import com.bezkoder.spring.entity.model.DataProdusiBeras;
import com.bezkoder.spring.entity.repo.DataProdusiBerasRepo;
import com.bezkoder.spring.login.models.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class DataProdusiBerasService {
    
    @Autowired
    private DataProdusiBerasRepo berasRepo;

    public DataProdusiBeras save(DataProdusiBeras beras){
        return berasRepo.save(beras);
    }

    public DataProdusiBeras findOne(Long id){
            Optional<DataProdusiBeras> beras = berasRepo.findById(id);
            if(!beras.isPresent()){
                return null;
            }
            return beras.get();
    }
    
    public Iterable<DataProdusiBeras> findAll(){
        return berasRepo.findAll();
    }

    public Iterable<DataProdusiBeras> findByPetani(User petani){
        return berasRepo.findByPetani(petani);
    }


    public void removeOne(Long id){
        berasRepo.deleteById(id);
    }
    
    public void updateStatusisTerjual(boolean status, Long id){
        berasRepo.updateStatusIsTerjual(status, id);
    }

    


}

