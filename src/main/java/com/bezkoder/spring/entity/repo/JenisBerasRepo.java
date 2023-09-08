package com.bezkoder.spring.entity.repo;




import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.bezkoder.spring.entity.model.JenisBeras;

public interface JenisBerasRepo extends CrudRepository<JenisBeras, Long>  {
    boolean existsById(Long id);
    Page<JenisBeras> findByNamaContains(String name, Pageable pageable);
}
    
