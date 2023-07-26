package com.bezkoder.spring.entity.repo;




import com.bezkoder.spring.entity.model.DataBeras;
import com.bezkoder.spring.entity.model.DataPenjualanBeras;
import com.bezkoder.spring.entity.model.DataProduksiBeras;
import com.bezkoder.spring.login.models.User;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface DataProduksiBerasRepo extends CrudRepository<DataProduksiBeras, Long>  {

    @Query(
    value = "select sum(dpb.berat_beras) as stok, dpb.jenis_beras_id as jenisBerasID, jb.nama as jenisBeras from sppb_mdataproduksiberas dpb join sppb_mjenisberas jb on (dpb.jenis_beras_id = jb.id) where dpb.is_terjual = 0 GROUP BY dpb.jenis_beras_id"
    ,nativeQuery = true)
    List<DataBeras> sumStokBeras();

    @Query(
        value = "select count(dpb.is_terjual) as berasTerjual, dpb.jenis_beras_id as jenisBerasID, jb.nama as jenisBeras, sum(dpb.berat_beras) as totalBerat, EXTRACT(MONTH FROM dpb.tanggal_masuk) as bulan, EXTRACT(Year FROM dpb.tanggal_masuk) as tahun  from sppb_mdataproduksiberas dpb join sppb_mjenisberas jb on (dpb.jenis_beras_id = jb.id) where dpb.is_terjual = 1 GROUP BY dpb.jenis_beras_id, bulan, tahun ORDER BY bulan asc, tahun asc",
        nativeQuery = true
        )
    List<DataPenjualanBeras> dataPenjualanBeras();

    Iterable<DataProduksiBeras> findByPetani(User petani);

   @Modifying
   @Query("update DataProduksiBeras dpb set dpb.isTerjual = :status where dpb.id = :id")
   void updateStatusIsTerjual (@Param("status") boolean status, @Param("id") long id);
}
    
