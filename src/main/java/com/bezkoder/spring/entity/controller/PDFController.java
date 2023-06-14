package com.bezkoder.spring.entity.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.bezkoder.spring.entity.model.DataBeras;
import com.bezkoder.spring.entity.model.DataPenjualanBeras;
import com.bezkoder.spring.entity.model.DataProdusiBeras;
import com.bezkoder.spring.entity.repo.DataProdusiBerasRepo;
import com.bezkoder.spring.entity.service.DataProdusiBerasService;
import com.bezkoder.spring.entity.util.DataBerasPDFExporter;
import com.bezkoder.spring.entity.util.DataPenjualanBerasPDFExporter;
import com.bezkoder.spring.entity.util.UserPDFExporter;
import com.lowagie.text.DocumentException;

@Controller
public class PDFController {

    @Autowired
    private DataProdusiBerasService service;

    @Autowired
    private DataProdusiBerasRepo repo;
    
    @GetMapping("/api/listBeras/export/pdf")
    @PreAuthorize("hasRole('ADMIN')")
    public void exportListBerasToPDF(HttpServletResponse response) throws DocumentException, IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
         
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        Iterable<DataProdusiBeras> listBerasIterable = service.findAll();

        List<DataProdusiBeras> listBeras = new ArrayList<>();
         
        listBerasIterable.forEach(listBeras::add);

        
         
        UserPDFExporter exporter = new UserPDFExporter(listBeras);
        exporter.export(response);
         
    }

    @GetMapping("/api/listDataBeras/export/pdf")
    @PreAuthorize("hasRole('ADMIN')")
    public void exportlistDataBerasToPDF(HttpServletResponse response) throws DocumentException, IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
         
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        List<DataBeras> listBeras = repo.sumStokBeras();
        
         
        DataBerasPDFExporter exporter = new DataBerasPDFExporter(listBeras);
        exporter.export(response);
         
    }

    @GetMapping("/api/listDataPenjualanBeras/export/pdf")
    @PreAuthorize("hasRole('ADMIN')")
    public void exportlistDataPenjualanBerasToPDF(HttpServletResponse response) throws DocumentException, IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
         
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        List<DataPenjualanBeras> listBeras = repo.dataPenjualanBeras();
        
         
        DataPenjualanBerasPDFExporter exporter = new DataPenjualanBerasPDFExporter(listBeras);
        exporter.export(response);
         
    }
}
