package com.bezkoder.spring.entity.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class PenjualanBeras {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private DataProduksiBeras beras;

    @Column(name = "tanggal_terjual", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime tanggal_terjual;

    
    
}
