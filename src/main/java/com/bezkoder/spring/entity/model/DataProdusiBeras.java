package com.bezkoder.spring.entity.model;


import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;


import java.time.LocalDateTime;

import com.bezkoder.spring.login.models.User;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Getter;
import lombok.Setter;

@Entity
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class, property = "id"
)

@Setter
@Getter
public class DataProdusiBeras {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "tanggal_masuk", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime tanggal_masuk;

    @ManyToOne
    private JenisBeras jenisBeras;

    private float berat_beras;

    private Integer harga;

    @Column(columnDefinition="BOOLEAN DEFAULT false")
    private boolean isTerjual;

    @ManyToOne
    private User petani;

    public DataProdusiBeras() {
    }

    public DataProdusiBeras(float berat_beras, Integer harga) {
        this.berat_beras = berat_beras;
        this.harga = harga;
    }
    
}
