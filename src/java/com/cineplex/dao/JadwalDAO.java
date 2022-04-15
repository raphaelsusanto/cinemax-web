/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cineplex.dao;

import com.cineplex.entity.DetailPesanan;
import com.cineplex.entity.Jadwal;
import com.cineplex.entity.Jam;
import com.cineplex.entity.MapSeat;
import com.cineplex.util.HibernateUtil;
import com.thoughtworks.xstream.XStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 *
 * @author raphael
 */
public class JadwalDAO {
    static final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    private Session beginSession() {
        return sessionFactory.openSession();
    }
    public List<Jadwal> getJadwal(int idFilm,int idBioskop,Date date){
        Session sess=beginSession();
        Query q= sess.createQuery("select j from Jadwal j join j.film f join j.studio s join s.bioskop b where f.idFilm=:idFilm and b.idBioskop=:idBioskop and j.tglMulai<=:tgl and j.tglAkhir>=:tgl");
        q.setInteger("idFilm", idFilm);
        q.setInteger("idBioskop", idBioskop);
        q.setDate("tgl", date);
        return q.list();
    }
    public List<Jadwal> getJadwalByBioskop(int idBioskop){
        Session sess=beginSession();
        //Query q= sess.createQuery("select j from Jadwal j join j.film f join j.studio s join s.bioskop b where b.idBioskop=:idBioskop and j.tglMulai<=:tgl");
        Query q= sess.createQuery("select j from Jadwal j join j.film f join j.studio s join s.bioskop b where b.idBioskop=:idBioskop and j.tglAkhir>=:tgl");
        q.setInteger("idBioskop", idBioskop);
        q.setDate("tgl", new Date());
        return q.list();
    }
    
    public List<Jam> getJam(int idFilm,int idBioskop,Date d){
        List<Jadwal> data=getJadwal(idFilm, idBioskop,d);
        List<Jam> dataJam= new ArrayList<Jam>();
        for (Jadwal j : data) {
            String[] temp=j.getJam().split("#");
            for (String string : temp) {
                Jam jam= new Jam();
                jam.setThree(j.getThree());
                jam.setIdJadwal(j.getIdJadwal());
                jam.setJam(string);
                List<DetailPesanan> dp=new PesananDAO().getDetailPesananByJadwal(string, j.getIdJadwal(),d);           
                XStream x= new XStream();
                MapSeat[][] m=(MapSeat[][])x.fromXML(j.getStudio().getDenah());
                int jmlh=0;
                for (MapSeat[] mapSeats : m) {
                    for (MapSeat mapSeat : mapSeats) {
                        if (mapSeat.isStatus()) {
                            jmlh++;
                        }
                    }
                }
                jam.setTmptDudukk(jmlh-dp.size());
                Calendar c= Calendar.getInstance();
                
                Date currentDate= new Date();
                Date playingDate= d;
                String[] jams=jam.getJam().split("\\.");
                playingDate.setHours(Integer.parseInt(jams[0]));
                playingDate.setMinutes(Integer.parseInt(jams[1]));
                if (currentDate.before(playingDate)) {
                    dataJam.add(jam);
                }
                
            }
        }
        return dataJam;
    }
    
    public List<Date> getAllDate(int idFilm,int idBioskop){
        List<Jadwal> listJadwal=getJadwal(idFilm, idBioskop,new Date());
        List<Date> listDate= new ArrayList<Date>();
        Date maxDate= new Date();
        for (Jadwal jadwal : listJadwal) {
            if (jadwal.getTglAkhir().compareTo(maxDate)>=0) {
                maxDate=jadwal.getTglAkhir();
            }
        }
        Calendar c= Calendar.getInstance();
        
        while (c.getTime().compareTo(maxDate)<0) {           
            listDate.add(c.getTime());
            c.add(Calendar.DAY_OF_WEEK, 1);
        }
        listDate.add(c.getTime());
        System.out.println(maxDate);
        return listDate;
    }
    
    public Jadwal getById(int id){
        Session sess=beginSession();
        return (Jadwal)sess.load(Jadwal.class, id);
    }
    
    public void save(Jadwal jadwal){
        Session sess=beginSession();
        Transaction t=sess.beginTransaction();
        sess.save(jadwal);
        t.commit();
    }
    
    public Boolean cek(Date startDate, Date endDate,int idStudio){
        Session sess= beginSession();
        Query q= sess.createQuery("from Jadwal j where ((j.tglMulai<=:start and j.tglAkhir>=:start) or (j.tglMulai<=:end and j.tglAkhir>=:end)) and j.studio.idStudio=:studio");
        q.setInteger("studio", idStudio);
        q.setDate("start", startDate);
        q.setDate("end", endDate);
        for (Jadwal j:(List<Jadwal>)q.list()){
            System.out.println(j.getIdJadwal());
        }
        return q.list().isEmpty();
    }
    
    public void delete(int idJadwal){
        Session sess=beginSession();
        Transaction t=sess.beginTransaction();
        sess.delete(getById(idJadwal));
        t.commit();
    }
    
    public void update(Jadwal j){
        Session sess=beginSession();
        Transaction t=sess.beginTransaction();
        sess.update(j);
        t.commit();
    }
    public static void main(String[] args) {
     
        
        System.out.println(new JadwalDAO().getAllDate(15, 12));
         System.out.println(new JadwalDAO().getJadwal(15, 12, new Date()));
    }
    
}
