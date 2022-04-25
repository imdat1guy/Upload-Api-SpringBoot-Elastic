/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.demo.Models;

import java.time.LocalDateTime;

/**
 *
 * class to represent processed archive object to be posted to elasticSearch index
 */
public class Archive {
    private String id;
    private String filename;
    private String status;
    private LocalDateTime timestamp;
    private long remaining;
    private long duration;

    public Archive(String filename, String status, long remaining, long duration) {
        this.filename = filename;
        this.status = status;
        this.timestamp = LocalDateTime.now();
        this.remaining = remaining;
        this.duration = duration;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public long getRemaining() {
        return remaining;
    }

    public void setRemaining(long remaining) {
        this.remaining = remaining;
    }
    
    @Override
    public String toString(){
        return "{\"filename\": \"" + filename + "\", \"status\": \"" + status
                + "\",\"timestamp\": \"" + timestamp+ "\",\"remaining\": " + remaining 
                + ",\"duration\": " + duration +"}";
    }
}
