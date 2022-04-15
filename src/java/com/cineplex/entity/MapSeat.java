/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cineplex.entity;

/**
 *
 * @author raphael
 */
public class MapSeat {
    private boolean status;
    private String text;

    public MapSeat() {
    }

    public MapSeat(boolean status, String text) {
        this.status = status;
        this.text = text;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    
    
    
}
