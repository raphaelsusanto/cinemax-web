/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cineplex.dao;

import com.cineplex.entity.Member;
import com.cineplex.entity.Saldo;
import com.cineplex.util.HibernateUtil;
import java.util.Date;
import java.util.List;
import org.hibernate.*;

/**
 *
 * @author raphael
 */
public class MemberDAO {

    static final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    private Session beginSession() {
        return sessionFactory.openSession();
    }

    public void save(Member m, int saldo) {
        Session sess = beginSession();
        Transaction t = sess.beginTransaction();
        SQLQuery q=sess.createSQLQuery("insert into member values(:email,md5(:pass),:name,:birthday,:identity,:no,:gender,0)");
        q.setString("email", m.getEmail());
        q.setString("pass", m.getPassword());
        q.setString("name", m.getName());
        q.setDate("birthday", m.getBirthday());
        q.setString("identity", m.getIdentityType());
        q.setString("no", m.getNoIdentity());
        q.setBoolean("gender", m.getGender());
        q.executeUpdate();
        sess.save(new Saldo(m, saldo, new Date()));
        t.commit();
    }
    
    public boolean cekLogin(String email, String pass){
        Session sess = beginSession();
        SQLQuery q= sess.createSQLQuery("select * from member where email=:email and password=md5(:pass)");
        q.setString("email", email);
        q.setString("pass", pass);
        return !q.list().isEmpty();
    }
    
    public void update(Member m){
        Session sess=beginSession();
        Transaction t= sess.beginTransaction();
        sess.update(m);
        t.commit();
    }

    public Member getById(String email) {
        Session sess = beginSession();
        return (Member) sess.load(Member.class,email);
    }

    public List<Member> getAll() {
        Session sess = beginSession();
        Criteria c = sess.createCriteria(Member.class);
        return c.list();
    }

    public Long getSaldo(String email) {
        Session sess = beginSession();
        Query q = sess.createQuery("select sum(s.saldo) from Saldo s join s.member m where m.email=:email");
        Query q2 = sess.createQuery("select sum(p.hargaTotal) from Member m join m.pesanans p where m.email=:email");
        Long saldo = new Long(0);
        Long harga = new Long(0);
        q.setString("email", email);
        q2.setString("email", email);

        saldo = (Long) q.uniqueResult();

        if (q2.uniqueResult() != null) {
            harga = (Long) q2.uniqueResult();
        }

        return saldo - harga;
    }

    public boolean changePassword(String email, String old, String newPass) {
       
        Session sess = beginSession();
        boolean result = false;
        Member m = (Member) sess.load(Member.class, email);
        if (m.getPassword().equals(old)) {

            m.setPassword(newPass);
            Transaction t = sess.beginTransaction();
            sess.update(m);
            t.commit();
            result = true;
        }
        return result;
    }

    public void addSaldo(Member member, int saldo) {
        Session sess = beginSession();
        Transaction t = sess.beginTransaction();
        Saldo s= new Saldo();
        s.setMember(member);
        s.setTgl(new Date());
        s.setSaldo(saldo);
        sess.save(s);
        t.commit();
    }

    public static void main(String[] args) {
        //new MemberDAO().getSaldo("p");
        
        MemberDAO dao= new MemberDAO();
        System.out.println(dao.cekLogin("admin@admin.com", "a"));
//        Member m=dao.getById("admin@admin.com");
//        System.out.println(m.getPassword());
        //dao.addSaldo(m, 30000);
    }
}
