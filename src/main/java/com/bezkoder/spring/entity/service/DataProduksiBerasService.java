package com.bezkoder.spring.entity.service;

import java.util.Optional;

import javax.transaction.Transactional;

import com.bezkoder.spring.entity.model.DataProduksiBeras;
import com.bezkoder.spring.entity.repo.DataProduksiBerasRepo;
import com.bezkoder.spring.login.models.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class DataProduksiBerasService {
    
    @Autowired
    private DataProduksiBerasRepo berasRepo;

    public DataProduksiBeras save(DataProduksiBeras beras){
        return berasRepo.save(beras);
    }

    public DataProduksiBeras findOne(Long id){
            Optional<DataProduksiBeras> beras = berasRepo.findById(id);
            if(!beras.isPresent()){
                return null;
            }
            return beras.get();
    }
    
    public Iterable<DataProduksiBeras> findAll(){
        return berasRepo.findAll();
    }

    public Iterable<DataProduksiBeras> findByPetani(User petani){
        return berasRepo.findByPetani(petani);
    }


    public void removeOne(Long id){
        berasRepo.deleteById(id);
    }
    
    public void updateStatusisTerjual(boolean status, Long id){
        berasRepo.updateStatusIsTerjual(status, id);
    }

    


}

