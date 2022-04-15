/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cineplex.bean;

import com.cineplex.dao.BioskopDAO;
import com.cineplex.dao.FilmDAO;
import com.cineplex.dao.JadwalDAO;
import com.cineplex.dao.StudioDAO;
import com.cineplex.entity.Bioskop;
import com.cineplex.entity.Film;
import com.cineplex.entity.Jadwal;
import com.cineplex.entity.Studio;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author raphael
 */
@ViewScoped
@ManagedBean
public class AddJadwalBean implements Serializable {

    private Bioskop bioskop;
    private List<Studio> listStudio;
    private List<Film> listFilm;
    private Date date, tglMulai, tglAkhir;
    private List<String> listDate;
    private Date currentDate = new Date();
    private int idStudio;
    private int idFilm;
    private boolean three;
    private Film selectedFilm;

    public AddJadwalBean() {
        try {
            Integer id = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
            bioskop = new BioskopDAO().getById(id);
            listFilm = new FilmDAO().getAll();
            listStudio = new ArrayList<Studio>();
            for (Studio studio : new ArrayList<Studio>(bioskop.getStudios())) {
                if (studio.isStatus()) {
                    listStudio.add(studio);
                }
            }
            listDate = new ArrayList<String>();
        } catch (Exception e) {
        }
    }

    public Film getSelectedFilm() {
        return selectedFilm;
    }

    public void setSelectedFilm(Film selectedFilm) {
        this.selectedFilm = selectedFilm;
    }

    
    public Bioskop getBioskop() {
        return bioskop;
    }

    public void setBioskop(Bioskop bioskop) {
        this.bioskop = bioskop;
    }

    public List<Studio> getListStudio() {
        return listStudio;
    }

    public void setListStudio(List<Studio> listStudio) {
        this.listStudio = listStudio;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<String> getListDate() {
        return listDate;
    }

    public void setListDate(List<String> listDate) {
        this.listDate = listDate;
    }

    public List<Film> getListFilm() {
        return listFilm;
    }

    public void setListFilm(List<Film> listFilm) {
        this.listFilm = listFilm;
    }

    public int getIdStudio() {
        return idStudio;
    }

    public void setIdStudio(int idStudio) {
        
        this.idStudio = idStudio;
    }

    public int getIdFilm() {
        return idFilm;
    }

    public void setIdFilm(int idFilm) {
        FilmDAO filmDAO= new FilmDAO();
        selectedFilm=filmDAO.getById(idFilm);
        this.idFilm = idFilm;
    }

    public boolean isThree() {
        return three;
    }

    public void setThree(boolean three) {
        this.three = three;
    }

    public Date getTglAkhir() {
        return tglAkhir;
    }

    public void setTglAkhir(Date tglAkhir) {
        this.tglAkhir = tglAkhir;
    }

    public Date getTglMulai() {
        return tglMulai;
    }

    public void setTglMulai(Date tglMulai) {
        this.tglMulai = tglMulai;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }

    public void addTime() {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        String jam = "00" + c.get(Calendar.HOUR_OF_DAY);
        jam = jam.substring(jam.length() - 2, jam.length());
        String minute = "00" + c.get(Calendar.MINUTE);
        minute = minute.substring(minute.length() - 2, minute.length());
        String add = jam + "." + minute;


        if ((!listDate.isEmpty())) {
            String a = listDate.get(listDate.size() - 1);
            String arrBefore[] = a.split("\\.");
            String arrAdd[] = add.split("\\.");
            String intBefore = arrBefore[0] + arrBefore[1];
            String intAdd = arrAdd[0] + arrAdd[1];
            String durasi = toTime(new FilmDAO().getById(idFilm).getDurasi());
            if ((Integer.parseInt(intBefore) + Integer.parseInt(durasi)) <= Integer.parseInt(intAdd)) {
                System.out.println(Integer.parseInt(intBefore));
                System.out.println(Integer.parseInt(durasi));
                System.out.println(Integer.parseInt(intAdd));
                listDate.add(add);
            } else {
                System.out.println(Integer.parseInt(intBefore));
                System.out.println(Integer.parseInt(durasi));
                System.out.println(Integer.parseInt(intAdd));
                FacesMessage msg = new FacesMessage("failed", "time must bigger");
                FacesContext.getCurrentInstance().addMessage(null, msg);
            }
        } else {
            listDate.add(add);
        }
    }

    private String toTime(int minute) {
        String jam = "00"+(minute / 60);
        String menit = ("00"+(minute % 60)) ;
        jam=jam.substring(jam.length()-2, jam.length());
        menit=menit.substring(menit.length()-2, menit.length());
        return jam + menit;
    }

    public String save() {
        if (tglMulai == null || tglAkhir == null || listDate.isEmpty()) {
            FacesMessage msg = new FacesMessage("Data harus diisi lengkap", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return "AddJadwal";
        } else {
            String jam = "";
            Jadwal j = new Jadwal();
            j.setStudio(new StudioDAO().getStudioById(idStudio));
            j.setFilm(new FilmDAO().getById(idFilm));
            for (String s : listDate) {
                jam += s + "#";
            }
            j.setTglMulai(tglMulai);
            j.setTglAkhir(tglAkhir);
            j.setJam(jam);
            j.setThree(three);
            j.setStatus(true);

//        if (new JadwalDAO().cek(tglMulai, tglAkhir, idStudio)) {
            new JadwalDAO().save(j);
            FacesMessage msg = new FacesMessage("success", "success insert data");
            FacesContext.getCurrentInstance().addMessage(null, msg);

//        } else {
//            FacesMessage msg = new FacesMessage("failed", "");
//            FacesContext.getCurrentInstance().addMessage(null, msg);
//        }
            return "DetailBioskop";
        }



    }

    public String save2() {
        if (tglMulai == null || tglAkhir == null || listDate.isEmpty()) {
            FacesMessage msg = new FacesMessage("Data harus diisi lengkap", "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            return "AddJadwal";
        } else {
            String jam = "";
            Jadwal j = new Jadwal();
            j.setStudio(new StudioDAO().getStudioById(idStudio));
            j.setFilm(new FilmDAO().getById(idFilm));
            for (String s : listDate) {
                jam += s + "#";
            }
            j.setTglMulai(tglMulai);
            j.setTglAkhir(tglAkhir);
            j.setJam(jam);
            j.setThree(three);
            j.setStatus(true);

//        if (new JadwalDAO().cek(tglMulai, tglAkhir, idStudio)) {
            new JadwalDAO().save(j);
            FacesMessage msg = new FacesMessage("success", "success insert data");
            FacesContext.getCurrentInstance().addMessage(null, msg);

//        } else {
//            FacesMessage msg = new FacesMessage("failed", "");
//            FacesContext.getCurrentInstance().addMessage(null, msg);
//        }
            return "DetailBioskop";
        }
    }

    public static void main(String[] args) {
        String a = "11.15";
        String b[] = a.split("\\.");
        for (String string : b) {
            System.out.println(string);
        }
    }
}
