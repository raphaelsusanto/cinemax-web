/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cineplex.service;

import com.cineplex.dao.StudioDAO;
import com.cineplex.entity.MapSeat;
import com.cineplex.entity.Studio;
import com.thoughtworks.xstream.XStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author raphael
 */
public class StudioService {
    public String getStudioByJadwal(int id){
        StudioDAO dao= new StudioDAO();
        Studio s=dao.getStudioByJadwal(id);
        s.setBioskop(null);
        s.setJadwals(null);
        String str=new XStream().toXML(s);
       
        return str;
    }
    
    public String getNotAvaibleSeat(int idJadwal,String jam,String d){
        StudioDAO dao= new StudioDAO();
        SimpleDateFormat dateFormat= new SimpleDateFormat("dd/MM/yyyy");
        List<String> data= new ArrayList<String>();
        try {
            data = dao.getNotAvaibleSeat(idJadwal, jam,dateFormat.parse(d));
        } catch (ParseException ex) {
            Logger.getLogger(StudioService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new XStream().toXML(data);
    }
    
    public static void main(String[] args) {
        String s=new StudioService().getStudioByJadwal(2);
        Studio st=(Studio) new XStream().fromXML(s);
        MapSeat[][] m=(MapSeat[][])new XStream().fromXML(st.getDenah());
        System.out.println(st.getDenah());
    }
}
