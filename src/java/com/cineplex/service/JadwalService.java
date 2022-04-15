/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cineplex.service;

import com.cineplex.dao.JadwalDAO;
import com.cineplex.entity.Jadwal;
import com.cineplex.entity.Jam;
import com.lowagie.text.pdf.ArabicLigaturizer;
import com.thoughtworks.xstream.XStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author raphael
 */
public class JadwalService {

    public String getJadwal(int idFilm, int idBioskop, String d) {
        JadwalDAO dao = new JadwalDAO();
        SimpleDateFormat dateFormat= new SimpleDateFormat("dd/MM/yyyy");
        List<Jadwal> data= new ArrayList<Jadwal>();
        try {
            data = dao.getJadwal(idFilm, idBioskop, dateFormat.parse(d));
        } catch (ParseException ex) {
            Logger.getLogger(JadwalService.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (Jadwal jadwal : data) {
            jadwal.setDetailPesanans(null);
            jadwal.setFilm(null);
            jadwal.setStudio(null);
        }
        XStream x = new XStream();
        return x.toXML(data);
    }

    public String getJam(int idFilm, int idBioskop,String d) {
        JadwalDAO dao = new JadwalDAO();
        SimpleDateFormat dateFormat= new SimpleDateFormat("dd/MM/yyyy");
        List<Jam> data= new ArrayList<Jam>();
        try {
             data = dao.getJam(idFilm, idBioskop,dateFormat.parse(d));
        } catch (ParseException ex) {
            Logger.getLogger(JadwalService.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        XStream x = new XStream();

        return x.toXML(data);
    }

    public String getAllDate(int idFilm, int idBioskop) {
        JadwalDAO dao = new JadwalDAO();
        List<Date> data = dao.getAllDate(idFilm, idBioskop);
        XStream x = new XStream();

        return x.toXML(data);
    }
}
