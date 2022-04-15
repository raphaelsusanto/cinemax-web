/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cineplex.dao;

import com.cineplex.entity.Holiday;
import com.cineplex.util.HibernateUtil;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 *
 * @author raphael
 */
public class HolidayDAO {
    static final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    
    public void save(Holiday h){
        Session sess= sessionFactory.openSession();
        Transaction t=sess.beginTransaction();
        sess.save(h);
        t.commit();
    }
    
    public List<String> getAll(){
        Session sess= sessionFactory.openSession();
        Criteria c= sess.createCriteria(Holiday.class);
        List<String> listDate= new ArrayList<String>();
        SimpleDateFormat dateFormat= new SimpleDateFormat("dd/MM/yyyy");
       
        for (Object h : c.list()) {
            listDate.add(dateFormat.format(((Holiday)h).getHolidayDate()));
        }
        return listDate;
    }
    
    public static void main(String[] args) {
        new HolidayDAO().save(new Holiday(new Date()));
    }
}
