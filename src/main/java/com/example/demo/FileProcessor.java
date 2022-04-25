/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.demo;

import com.example.demo.Elasticsearch.ElasticSearchClient;
import com.example.demo.Models.Archive;
import com.example.demo.Models.ProcessedFile;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;
import org.springframework.beans.factory.annotation.Autowired;


/**
 *
 * Zip file processor
 */
public class FileProcessor {
    private static final int BUFFER_SIZE = 4096;
    
    private ElasticSearchClient esClient;
    //private static final String destProcessed = "output/processed";
    //private static final String destFailed = "output/failed";

    public FileProcessor() {
        esClient = new ElasticSearchClient();
    }
    
    /**
     * Extracts a zip file specified by the zipFilePath to a directory specified by
     * destDirectory (will be created if does not exists)
     * @param zipFilePath
     */
    public void unzip(String zipFilePath) {
        Instant start = Instant.now();
        try{
            File destDir = new File(ApplicationConfig.StorageFolder);
            if (!destDir.exists()) {
                destDir.mkdir();
            }
            ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
            try{
                ZipEntry entry = zipIn.getNextEntry();
                String zipName = zipFilePath.substring(zipFilePath.lastIndexOf(File.separator) + 1);
                ArrayList<ProcessedFile> pFiles = new ArrayList<ProcessedFile>();
                //iterate over entries in the zip file
                while (entry != null) {
                    if (!entry.isDirectory()) {
                        // make sure zipEntrry is a file
                        String fileName = entry.getName();
                        String fileType =  fileName.substring(fileName.lastIndexOf(".") + 1);
                        String fileDir = ApplicationConfig.StorageFolder + File.separator + fileType;
                        File fileDestDir = new File(fileDir);
                        fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
                        
                        if (!fileDestDir.exists()) {
                            fileDestDir.mkdir();
                        }
                        String FilePath = fileDestDir + File.separator + zipName.substring(0, zipName.lastIndexOf('.')) + "_" +fileName;
                        extractFile(zipIn, FilePath);
                        
                        //add file to list to later index. Content is empty as parsing is not implemented
                        pFiles.add(new ProcessedFile(fileName, fileType, FilePath, "", ""));
                    }
                    zipIn.closeEntry();
                    entry = zipIn.getNextEntry();
                }
                zipIn.close();
                
                //success processing archive. Move to processed
                File passDestDir = new File(ApplicationConfig.StorageFolder + File.separator + "processed");
                if (!passDestDir.exists()) {
                    passDestDir.mkdir();
                }
                File archive = new File(zipFilePath);
                System.out.println(archive);
                String newPath = passDestDir + File.separator+ zipFilePath.substring(zipFilePath.lastIndexOf(File.separator) + 1);
                System.out.println(newPath);
                if(archive.renameTo(new File(newPath))){
                    System.out.println("success moving processed archive");
                }else{
                    System.out.println("fail moving processed archive");
                }
                
                Instant end = Instant.now();
                
                //index archive
                long remaining = Files.list(Paths.get(ApplicationConfig.InputFolder)).count();
                Archive arch = new Archive(zipName,"processed",remaining, Duration.between(start, end).toMillis());
                String archID = esClient.saveDocument("archives", arch.toString());
                
                //index files. (This should be changed to bulk indexing later.
                pFiles.forEach(pFile -> {
                    pFile.setArchiveID(archID);
                    esClient.saveDocument("files", pFile.toString());
                });
                
            } catch(IOException ex){
                //error processing zip entries. Move Archive to fail
                System.out.println("We out here fail: " + ex.getMessage());
                File failDestDir = new File(ApplicationConfig.StorageFolder + File.separator + "failed");
                if (!failDestDir.exists()) {
                    failDestDir.mkdir();
                }
                zipIn.close();
                File archive = new File(zipFilePath);
                //System.out.println(archive);
                String newPath = failDestDir+ File.separator + zipFilePath.substring(zipFilePath.lastIndexOf(File.separator) + 1);
                //System.out.println(newPath);
                if(archive.renameTo(new File(newPath))){
                    System.out.println("success moving failed archive");
                }else{
                    System.out.println("fail moving failed archive");
                }
                
                Instant end = Instant.now();
                //record in db
                long remaining = Files.list(Paths.get(ApplicationConfig.InputFolder)).count();
                Archive arch = new Archive(zipFilePath.substring(zipFilePath.lastIndexOf(File.separator) + 1),"failed",remaining, Duration.between(start, end).toMillis());
                esClient.saveDocument("archives", arch.toString());
            }
            zipIn.close();
        }catch(IOException e){
           System.out.println("Issue finding zip file: " + zipFilePath);
        }
    }
    /**
     * Extracts a zip entry (file entry)
     * @param zipIn
     * @param filePath
     * @throws IOException
     */
    private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }
}
