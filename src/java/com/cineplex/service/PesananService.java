/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cineplex.service;

import com.cineplex.dao.HolidayDAO;
import com.cineplex.dao.JadwalDAO;
import com.cineplex.dao.MemberDAO;
import com.cineplex.dao.PesananDAO;
import com.cineplex.entity.DetailPesanan;
import com.cineplex.entity.Jadwal;
import com.cineplex.entity.Member;
import com.cineplex.entity.Pesanan;
import com.thoughtworks.xstream.XStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author raphael
 */
public class PesananService {

    public String pesan(int idJadwal, String email, String[] no, String jam, String d) {

        SimpleDateFormat dateFormat= new SimpleDateFormat("dd/MM/yyyy");
        HashSet<DetailPesanan> detail = new HashSet<DetailPesanan>();
        MemberDAO memberDAO = new MemberDAO();
        Member member = memberDAO.getById(email);
        Pesanan pesan = new Pesanan();
        pesan.setMember(member);
        pesan.setCetak(false);
        pesan.setDetailPesanans(null);
        pesan.setJam(jam);
        try {
            pesan.setTanggal(dateFormat.parse(d));
        } catch (ParseException ex) {
            Logger.getLogger(PesananService.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (String noKursi : no) {
            DetailPesanan dp = new DetailPesanan();
            dp.setJadwal(new JadwalDAO().getById(idJadwal));
            dp.setNoKursi(noKursi);
            dp.setPesanan(pesan);
            detail.add(dp);
        }
        pesan.setDetailPesanans(detail);

        String result = new PesananDAO().savePesanan(pesan);
        XStream x = new XStream();

        return x.toXML(result);
    }

    public String getDetailOrder(int idJadwal,String d) {
         SimpleDateFormat dateFormat= new SimpleDateFormat("dd/MM/yyyy");
        JadwalDAO jadwalDAO = new JadwalDAO();
        Jadwal jadwal = jadwalDAO.getById(idJadwal);
        String judulFilm = jadwal.getFilm().getJudul();
        String namaTheater = jadwal.getStudio().getBioskop().getNama();

        HolidayDAO holidayDAO = new HolidayDAO();
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(dateFormat.parse(d));
        } catch (ParseException ex) {
            Logger.getLogger(PesananService.class.getName()).log(Level.SEVERE, null, ex);
        }
        int day = cal.get(Calendar.DAY_OF_WEEK);
        // jadwal.getDetailPesanans().iterator();
        int harga = 0;
        if (jadwal.getThree()) {
            if (holidayDAO.getAll().contains(d)) {
                harga = jadwal.getStudio().getBioskop().getHtm3d();
            } else {
                if (day >= 2 && day <= 5) {
                    harga = jadwal.getStudio().getBioskop().getNomat3d();
                } else if (day == 6) {
                    harga = jadwal.getStudio().getBioskop().getJmt3d();
                } else {
                    harga = jadwal.getStudio().getBioskop().getHtm3d();
                }
            }
        } else {
            if (holidayDAO.getAll().contains(d)) {
                harga = jadwal.getStudio().getBioskop().getHtm();
            } else {
                if (day >= 2 && day <= 5) {
                    harga = jadwal.getStudio().getBioskop().getNomat();
                } else if (day == 6) {
                    harga = jadwal.getStudio().getBioskop().getJmt();
                } else {
                    harga = jadwal.getStudio().getBioskop().getHtm();
                }
            }
        }


        Integer hargaSatuan = harga;
        Date tgl= new Date();
        try {
            tgl = dateFormat.parse(d);
        } catch (ParseException ex) {
            Logger.getLogger(PesananService.class.getName()).log(Level.SEVERE, null, ex);
        }
        XStream x = new XStream();
        return x.toXML(new Object[]{judulFilm, namaTheater, hargaSatuan, tgl});
    }

    public String getOrderHistory(String email) {
        int saldo = 0;
        PesananDAO dao = new PesananDAO();
        List<Object[]> data = dao.getOrderHistory(email);
        List<Object[]> result = new ArrayList<Object[]>();
        Collections.reverse(data);

        for (Object[] objects : data) {
            if (objects[2].toString().equals("1")) {
                saldo += (Integer) objects[0];
            } else {
                saldo -= (Integer) objects[0];
            }
            result.add(new Object[]{objects[0], objects[1], objects[2], saldo, objects[4]});
        }
        //Collections.reverse(result);
        return new XStream().toXML(result);
    }

    public String getDetailHistoryOrder(String idOrder) {
        PesananDAO pesananDAO = new PesananDAO();
        Pesanan p = pesananDAO.getById(idOrder);
        String seat = "";
        List<DetailPesanan> listDetailPesanan = new ArrayList<DetailPesanan>(p.getDetailPesanans());
        for (DetailPesanan detailPesanan : listDetailPesanan) {
            seat += detailPesanan.getNoKursi() + ",";
        }
        seat = seat.substring(0, seat.length() - 1);
        DetailPesanan dp = listDetailPesanan.get(0);
        String judulFilm = dp.getJadwal().getFilm().getJudul();
        String namaTheater = dp.getJadwal().getStudio().getBioskop().getNama();
        String jam = p.getJam();
        String date = SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT, new Locale("in", "ID")).format(p.getTanggal());
        return new XStream().toXML(new String[]{judulFilm, namaTheater, date, jam, seat});
    }

    public static void main(String[] args) {
//        List<String>list= new ArrayList<String>();
//        list.add("A1");
//        list.add("A2");
////        new PesananService().pesan(1, "p", list, "13.00");
//        Calendar cal = Calendar.getInstance();
//        int day = cal.get(Calendar.DAY_OF_WEEK);
//        System.out.println(day);
//        PesananService p = new PesananService();
//        System.out.println(p.getDetailHistoryOrder(58));
    }
}
