/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.demo;

import java.io.File;
import java.util.Set;
import org.springframework.boot.devtools.filewatch.ChangedFile;
import org.springframework.boot.devtools.filewatch.ChangedFiles;
import org.springframework.boot.devtools.filewatch.FileChangeListener;

/**
 *
 * @author PC
 */
public class MyFileChangeListener implements FileChangeListener {
    private FileProcessor fileProcessor;

    public MyFileChangeListener(FileProcessor fileProcessor) {
        this.fileProcessor = fileProcessor;
    }
    
    @Override
    public void onChange(Set<ChangedFiles> changeSet) {
        for(ChangedFiles cfiles:changeSet) {
            for(ChangedFile cfile:cfiles.getFiles()) {
                String FileName = cfile.getFile().getName();
                System.out.println(cfile.getType()+":"+FileName);
                
                //process added zip files
                if(cfile.getType() == ChangedFile.Type.ADD && FileName.endsWith(".zip")){
                    fileProcessor.unzip(ApplicationConfig.InputFolder + File.separator + FileName);
                }
                
            }
        }
    }
}
