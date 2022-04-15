/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cineplex.bean;

import com.cineplex.dao.PesananDAO;
import com.cineplex.entity.DetailPesanan;
import com.cineplex.entity.Film;
import com.cineplex.entity.Jadwal;
import com.cineplex.entity.Pesanan;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author raphael
 */
@ManagedBean
public class ManageTransaksiBean implements Serializable{
    private List<Pesanan> listPesanan;
    private Pesanan selectedPesanan;
    private Jadwal jadwal;
    private String seat;

    public ManageTransaksiBean() {
        listPesanan= new PesananDAO().getAllPesanan();
    }

    public List<Pesanan> getListPesanan() {
        return listPesanan;
    }

    public void setListPesanan(List<Pesanan> listPesanan) {
        this.listPesanan = listPesanan;
    }

    public Pesanan getSelectedPesanan() {
        return selectedPesanan;
    }

    public void setSelectedPesanan(Pesanan selectedPesanan) {
        this.selectedPesanan = selectedPesanan;
        jadwal=new ArrayList<DetailPesanan>(selectedPesanan.getDetailPesanans()).get(0).getJadwal();
        Iterator<DetailPesanan> it=selectedPesanan.getDetailPesanans().iterator();
        seat="";
        while (it.hasNext()) {            
            seat+=it.next().getNoKursi()+",";
        }
        seat=seat.substring(0, seat.length()-1);
    }

    public Jadwal getJadwal() {
        return jadwal;
    }

    public void setJadwal(Jadwal jadwal) {
        this.jadwal = jadwal;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }
    
   
}
