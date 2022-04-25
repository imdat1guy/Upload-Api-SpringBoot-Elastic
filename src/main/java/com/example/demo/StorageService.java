/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/springframework/Service.java to edit this template
 */
package com.example.demo;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.atomic.AtomicLong;
/**
 *
 * @author PC
 */
@Service
public class StorageService {
    private final Path fileStorageLocation;
    private final AtomicLong counter = new AtomicLong();
    //@Autowired
    //DocumentStoragePropertiesRepo docStorageRepo;
    @Autowired
    public StorageService() throws Exception {
        this.fileStorageLocation = Paths.get(ApplicationConfig.InputFolder)
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException ex) {
            throw new Exception("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }
    public String storeFile(MultipartFile file, String docType) throws Exception {
        // Normalize file name
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileName = "";
        try {
            // Check if the file's name contains invalid character
            if(originalFileName.contains("..")) {
                throw new Exception("Sorry! Filename contains invalid path sequence " + originalFileName);
            }
            fileName = counter.incrementAndGet() + "_" + originalFileName;
         // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            
            return fileName;
        } catch (IOException ex) {
            throw new Exception("Could not store file " + fileName + ". Please try again!", ex);
        }
    }
}
