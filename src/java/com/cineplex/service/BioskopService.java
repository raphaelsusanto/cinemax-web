/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cineplex.service;

import com.cineplex.dao.BioskopDAO;
import com.cineplex.entity.Bioskop;
import com.thoughtworks.xstream.XStream;
import java.util.List;

/**
 *
 * @author raphael
 */
public class BioskopService {
    public String getAll(){
        BioskopDAO dao= new BioskopDAO();
        List<Bioskop> data=dao.getAllNowPlaying();
        for (Bioskop bioskop : data) {
            bioskop.setStudios(null);
        }
        XStream xStream= new XStream();
        return xStream.toXML(data);
    }
    
    public String getBioskopById(int id){
        BioskopDAO dao= new BioskopDAO();
        Bioskop b=dao.getById(id);
        b.setStudios(null);
        XStream xStream= new XStream();
        return xStream.toXML(b);
    }
    
    public String getAllByFilm(int id){
        BioskopDAO dao= new BioskopDAO();
        List<Bioskop>data=dao.getAllByFilm(id);
        for (Bioskop bioskop : data) {
            bioskop.setStudios(null);
        }
        XStream x= new XStream();
        return x.toXML(data);
    }
}
