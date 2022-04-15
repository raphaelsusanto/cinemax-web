/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cineplex.dao;

import com.cineplex.entity.Bioskop;
import com.cineplex.util.HibernateUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import org.hibernate.*;

/**
 *
 * @author raphael
 */
public class BioskopDAO {

    static final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    private Session beginSession() {
        return sessionFactory.openSession();
    }

    public List<Bioskop> getAllNowPlaying() {
        Session sess = beginSession();
        Query q = sess.createQuery("select b from Bioskop b join b.studios s join s.jadwals j where j.tglMulai<= :tgl and j.tglAkhir>= :tgl and j.status=true");
        q.setDate("tgl", new Date());
        HashSet<Bioskop> b = new HashSet<Bioskop>(q.list());
        return new ArrayList<Bioskop>(b);
    }

    public List<Bioskop> getAll() {
        Session sess = beginSession();
        Criteria c = sess.createCriteria(Bioskop.class);
        return c.list();
    }

    public void save(Bioskop b) {
        Session sess = beginSession();
        Transaction t = sess.beginTransaction();
        sess.save(b);
        t.commit();
    }

    public void update(Bioskop b) {
        Session sess = beginSession();
        Transaction t = sess.beginTransaction();
        sess.update(b);
        t.commit();
    }

    public Bioskop getById(int id) {
        Session sess = beginSession();
        return (Bioskop) sess.load(Bioskop.class, id);
    }

    public List<Bioskop> getAllByFilm(int id) {
        Session sess = beginSession();
        Query q = sess.createQuery("select b from Bioskop b join b.studios s join s.jadwals j join j.film f where f.idFilm=:id and j.tglMulai<= :tgl and j.tglAkhir>= :tgl and j.status=true");
        q.setInteger("id", id);
        q.setDate("tgl", new Date());
        HashSet<Bioskop> b = new HashSet<Bioskop>(q.list());
        return new ArrayList<Bioskop>(b);
    }

    public void delete(Bioskop bioskop) {
        Session sess = beginSession();
        Transaction t = sess.beginTransaction();
        sess.delete(bioskop);
        t.commit();
    }

    public static void main(String[] args) {
        new BioskopDAO().getAllByFilm(1);
    }
}
