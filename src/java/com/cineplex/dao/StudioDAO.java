/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cineplex.dao;

import com.cineplex.entity.Bioskop;
import com.cineplex.entity.Studio;
import com.cineplex.util.HibernateUtil;
import java.util.Date;
import java.util.List;
import org.hibernate.*;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author raphael
 */
public class StudioDAO {

    static final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    private Session beginSession() {
        return sessionFactory.openSession();
    }

    public Studio getStudioByJadwal(int id) {
        Session sess = beginSession();
        Query q = sess.createQuery("select s from Studio s join s.jadwals j where j.idJadwal=:idJadwal");
        q.setInteger("idJadwal", id);
        return (Studio) q.uniqueResult();
    }

    public Studio getStudioById(int id) {
        Session sess = beginSession();
        return (Studio) sess.load(Studio.class, id);
    }

    public List<String> getNotAvaibleSeat(int idJadwal, String jam, Date d) {
        Session sess = beginSession();
        Query q = sess.createQuery("select dp.noKursi from Jadwal j join j.detailPesanans dp join dp.pesanan p where j.idJadwal=:idJadwal and p.jam=:jam and  p.tanggal=:tgl");
        q.setInteger("idJadwal", idJadwal);
        q.setString("jam", jam);
        q.setDate("tgl", d);
        return q.list();
    }
    
    public List<Studio> getAllStudioByBioskop(Bioskop b){
        Session sess=beginSession();
        Criteria c=sess.createCriteria(Studio.class);
        c.add(Restrictions.eq("bioskop", b));
        return c.list();
    }

    public void save(Studio s) {
        Session sess = beginSession();
        Transaction t = sess.beginTransaction();
        sess.save(s);
        t.commit();
    }

    public void update(Studio s) {
        Session sess = beginSession();
        Transaction t = sess.beginTransaction();
        sess.update(s);
        t.commit();
    }
    
    public void delete(Studio s){
        Session sess = beginSession();
        Transaction t = sess.beginTransaction();
        sess.delete(s);
        t.commit();
    }

//    public static void main(String[] args) {
//        List<String> data = new StudioDAO().getNotAvaibleSeat(1, "13.00");
//        for (String string : data) {
//            System.out.println(string);
//        }
//    }
}
