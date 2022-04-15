/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cineplex.dao;

import com.cineplex.entity.DetailPesanan;
import com.cineplex.entity.Jadwal;
import com.cineplex.entity.Member;
import com.cineplex.entity.Pesanan;
import com.cineplex.entity.Saldo;
import com.cineplex.util.HibernateUtil;
import com.thoughtworks.xstream.XStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.hibernate.*;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author raphael
 */
public class PesananDAO {

    static final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    private Session beginSession() {
        return sessionFactory.openSession();
    }

    public List<DetailPesanan> getDetailPesananByJadwal(String jam, int idJadwal,Date d) {
        Session sess = beginSession();
        Query q = sess.createQuery("select dp from DetailPesanan dp join dp.jadwal j join dp.pesanan p where j.idJadwal=:idJadwal and p.jam=:jam and p.tanggal=:tgl");
        q.setInteger("idJadwal", idJadwal);
        q.setString("jam", jam);
        q.setDate("tgl", d);
        return q.list();
    }

    public String savePesanan(Pesanan p) {
        Session sess = beginSession();
        Transaction t = sess.beginTransaction();
        List<String> no = new ArrayList<String>();
        HolidayDAO holidayDAO = new HolidayDAO();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        int idJadwal = 0;
        String idPesanan = "";

        p.setTglJual(new Date());
        try {

            SQLQuery q = sess.createSQLQuery("call sp_addPesanan(:jam,:tgl,:harga,:tglJual,:email)");
            q.setParameter("jam", p.getJam());
            q.setParameter("tgl", p.getTanggal());
            q.setParameter("harga", 0);
            q.setParameter("tglJual", p.getTglJual());
            q.setParameter("email", p.getMember().getEmail());
            idPesanan = q.uniqueResult().toString();

            Iterator<DetailPesanan> it = p.getDetailPesanans().iterator();
            int count = 0;
            while (it.hasNext()) {
                DetailPesanan temp = it.next();
                SQLQuery q2 = sess.createSQLQuery("call sp_addDetailPesanan(:noKursi,:idJadwal,:idPesanan)");
                q2.setParameter("noKursi", temp.getNoKursi());
                q2.setParameter("idJadwal", temp.getJadwal().getIdJadwal());
                q2.setParameter("idPesanan", idPesanan);
                q2.uniqueResult();
                //sess.save(temp);
                no.add(temp.getNoKursi());
                count++;
            }


            Calendar cal = Calendar.getInstance();
            cal.setTime(p.getTanggal());
            int day = cal.get(Calendar.DAY_OF_WEEK);
            it = p.getDetailPesanans().iterator();
            int harga = 0;
            if (it.hasNext()) {
                Jadwal j = it.next().getJadwal();
                idJadwal = j.getIdJadwal();


                if (j.getThree()) {
                    if (holidayDAO.getAll().contains(dateFormat.format(p.getTanggal()))) {
                        harga = j.getStudio().getBioskop().getHtm3d();
                    } else {
                        if (day >= 2 && day <= 5) {
                            harga = j.getStudio().getBioskop().getNomat3d();
                        } else if (day == 6) {
                            harga = j.getStudio().getBioskop().getJmt3d();
                        } else {
                            harga = j.getStudio().getBioskop().getHtm3d();
                        }
                    }

                } else {
                    if (holidayDAO.getAll().contains(dateFormat.format(p.getTanggal()))) {
                        harga = j.getStudio().getBioskop().getHtm();
                    } else {
                        if (day >= 2 && day <= 5) {
                            harga = j.getStudio().getBioskop().getNomat();
                        } else if (day == 6) {
                            harga = j.getStudio().getBioskop().getJmt();
                        } else {
                            harga = j.getStudio().getBioskop().getHtm();
                        }
                    }
                }

            }

            if (new MemberDAO().getSaldo(p.getMember().getEmail()) >= (harga * count)) {
//                Saldo s= new Saldo();
//                s.setMember(p.getMember());
//                s.setSaldo(-1*harga*count);
//                sess.save(s);
            } else {
                return "Your account is not enough to do the order. Please top up your account";
            }
            p.setIdPesanan(idPesanan);
            p.setHargaTotal(harga * count);
            sess.update(p);
            StudioDAO studioDAO = new StudioDAO();
            boolean cek = true;
            for (String num : no) {
                if (studioDAO.getNotAvaibleSeat(idJadwal, p.getJam(),p.getTanggal()).contains(num)) {
                    cek = false;
                    break;
                }
            }

            if (cek) {
                t.commit();
            } else {
                return "Your order is failed. The seat that you chose is already been taken";
            }

            return "Your order is success. You can print your ticket at the CInema.";
        } catch (Exception e) {
            t.rollback();
            e.printStackTrace();
            return e.toString() + "|-|-|-|" + idPesanan;
        }

    }

    public List<Object[]> getOrderHistory(String email) {
        Session sess = beginSession();
        SQLQuery query = sess.createSQLQuery("select * from (select saldo as 'saldo', tgl as 'tanggal', '1' as 'desc',email,'0' as 'id'  from saldo union select harga_total as 'saldo',tgl_jual as 'tanggal', '0' as 'desc',email,id_pesanan as 'id' from pesanan) as tbl where email=:email order by tbl.tanggal desc");
        query.setString("email", email);
        return query.list();
    }

    public List<Pesanan> getAllPesanan() {
        Session sess = beginSession();
        Criteria c = sess.createCriteria(Pesanan.class);
        return c.list();
    }

    public List<Pesanan> getPesananByMember(Member m) {
        Session sess = beginSession();
        Criteria c = sess.createCriteria(Pesanan.class).add(Restrictions.eq("member", m));
        c.add(Restrictions.eq("cetak", false));
        return c.list();
    }

    public void update(Pesanan p) {
        Session sess = beginSession();
        Transaction t = sess.beginTransaction();
        sess.update(p);
        t.commit();
    }

    public Pesanan getById(String id) {
        Session sess = beginSession();
        return (Pesanan) sess.load(Pesanan.class, id);
    }

    public void cek() {

        HolidayDAO holidayDAO = new HolidayDAO();
        Date d = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.getTime();
        System.out.println(c.getTime() + "");
//        for (Date date : holidayDAO.getAll()) {
//            System.out.println(date+"");
//        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        System.out.println(holidayDAO.getAll().contains(dateFormat.format(d)) + "");
        holidayDAO.getAll().contains(new Date());
    }

    public static void main(String[] args) {
//        List<Pesanan> data = new PesananDAO().getPesananByMember(new MemberDAO().getById("p"));
//        for (Pesanan object : data) {
//            System.out.println(object.getIdPesanan());
//        }
//        Pesanan p = new PesananDAO().getById(15);
//        p.setCetak(Boolean.TRUE);
//        XStream x = new XStream();
//        Pesanan a = (Pesanan) x.fromXML(x.toXML(p));
//        new PesananDAO().update(a);
        new PesananDAO().cek();
    }
}
