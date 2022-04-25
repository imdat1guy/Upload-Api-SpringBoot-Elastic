/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.demo.Models;

import java.time.LocalDateTime;

/**
 *
 * class to represent processed file/document object to be posted to elasticSearch index
 */
public class ProcessedFile {
    private String filename;
    private String archiveID;
    private String fileType;
    private String filePath;
    private String content; 
    private LocalDateTime timestamp;

    public ProcessedFile(String filename, String type, String path, String archiveID, String content) {
        this.filename = filename;
        this.archiveID = archiveID;
        this.fileType = type;
        this.filePath = path.replace('\\', '/'); //replace backward slash to avoid json parsing error
        this.content = content;
        this.timestamp = LocalDateTime.now();
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getArchiveID() {
        return archiveID;
    }

    public void setArchiveID(String archiveID) {
        this.archiveID = archiveID;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    @Override
    public String toString(){
        return "{\"filename\": \"" + filename 
                + "\",\"fileType\": \"" + fileType
                + "\",\"filePath\": \"" + filePath
                + "\",\"archiveID\": \"" + archiveID
                + "\",\"timestamp\": \"" + timestamp
                + "\",\"content\": \"" + content
                + "\"}";
    }
}
