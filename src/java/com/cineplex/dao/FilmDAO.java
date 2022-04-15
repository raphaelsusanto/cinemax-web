/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cineplex.dao;

import com.cineplex.entity.Film;
import com.cineplex.util.HibernateUtil;
import java.util.*;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 *
 * @author raphael
 */
public class FilmDAO {

    static final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    private Session beginSession() {
        return sessionFactory.openSession();
    }

    public List<Film> getAll() {
        Session sess = beginSession();
        Criteria c = sess.createCriteria(Film.class);
        return c.list();
    }

    public void save(Film f) {
        Session sess = beginSession();
        Transaction t = sess.beginTransaction();
        sess.save(f);
        t.commit();
    }

    public void update(Film f) {
        Session sess = beginSession();
        Transaction t = sess.beginTransaction();
        sess.update(f);
        t.commit();
    }

    public Film getById(int id) {
        Session sess = beginSession();
        return (Film) sess.load(Film.class, id);
    }

    public List<Film> getNowPlaying() {
        Session sess = beginSession();
        Query q = sess.createQuery("select f from Film f join f.jadwals  j where j.tglMulai<= :tgl and j.tglAkhir>= :tgl and j.status=true");
        q.setDate("tgl", new Date());
        HashSet<Film> f = new HashSet<Film>(q.list());
        List data=new ArrayList<Film>(f);
        Collections.sort(data);
        return data;
    }

    public List<Film> getNowPlayingByBioskop(int id) {
        Session sess = beginSession();
        Query q = sess.createQuery("select f from Film f join f.jadwals  j join j.studio s join s.bioskop b where b.idBioskop=:id and j.tglMulai<= :tgl and j.tglAkhir>= :tgl and j.status=true");
        q.setInteger("id", id);
        q.setDate("tgl", new Date());
        HashSet<Film> f = new HashSet<Film>(q.list());
        return new ArrayList<Film>(f);
    }

    public List<Film> getCommingSoon() {
        Session sess = beginSession();
        Query q = sess.createQuery("from Film film where film.id not in (select f.id from Film f join f.jadwals j)");
        HashSet<Film> f = new HashSet<Film>(q.list());
        return new ArrayList<Film>(f);
    }

    public void delete(Film f) {
        Session sess = beginSession();
        Transaction t = sess.beginTransaction();
        sess.delete(f);
        t.commit();
    }

    public static void main(String[] args) {
        new FilmDAO().getNowPlaying();
    }
}
