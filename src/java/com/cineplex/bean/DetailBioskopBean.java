/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cineplex.bean;

import com.cineplex.dao.BioskopDAO;
import com.cineplex.dao.JadwalDAO;
import com.cineplex.dao.StudioDAO;
import com.cineplex.entity.Bioskop;
import com.cineplex.entity.DetailPesanan;
import com.cineplex.entity.Jadwal;
import com.cineplex.entity.Studio;
import com.thoughtworks.xstream.XStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author raphael
 */
@ViewScoped
@ManagedBean
public class DetailBioskopBean implements Serializable {

    private Bioskop bioskop;
    private List<Jadwal> listJadwal;
    private int idJadwal;
    private List<Studio> listStudio;
    private Studio selectedStudio;
    private int id;

    public DetailBioskopBean() {
        try {
            id = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
            bioskop = new BioskopDAO().getById(id);
            listJadwal = new JadwalDAO().getJadwalByBioskop(id);
            listStudio = new StudioDAO().getAllStudioByBioskop(bioskop);
        } catch (Exception e) {
        }
    }

    public Bioskop getBioskop() {
        return bioskop;
    }

    public void setBioskop(Bioskop bioskop) {
        this.bioskop = bioskop;
    }

    public int getIdJadwal() {
        return idJadwal;
    }

    public void setIdJadwal(int idJadwal) {
        this.idJadwal = idJadwal;
    }

    public List<Jadwal> getListJadwal() {
        return listJadwal;
    }

    public void setListJadwal(List<Jadwal> listJadwal) {
        this.listJadwal = listJadwal;
    }

    public List<Studio> getListStudio() {
        return listStudio;
    }

    public void setListStudio(List<Studio> listStudio) {
        this.listStudio = listStudio;
    }

    public Studio getSelectedStudio() {
        return selectedStudio;
    }

    public void setSelectedStudio(Studio selectedStudio) {
        this.selectedStudio = selectedStudio;
    }

    public void deleteJadwal() {

//        Jadwal j = new JadwalDAO().getById(idJadwal);
//        Iterator<DetailPesanan> it = j.getDetailPesanans().iterator();
//        boolean cek = false;
//        while (it.hasNext()) {
//            if (it.next().getPesanan().getTanggal() == new Date()) {
//                cek = true;
//                break;
//            }
//        }
//        if (!cek) {
//            new JadwalDAO().delete(idJadwal);
//            FacesMessage msg = new FacesMessage("delete", "successfull delete data");
//            FacesContext.getCurrentInstance().addMessage(null, msg);
//            listJadwal = new JadwalDAO().getJadwalByBioskop(bioskop.getIdBioskop());
//        } else {
//            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,"cannot delete Jadwal", "Already");
//            FacesContext.getCurrentInstance().addMessage(null, msg);
//        }

        try {
            new JadwalDAO().delete(idJadwal);
            FacesMessage msg = new FacesMessage("delete", "successfull delete data");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            listJadwal = new JadwalDAO().getJadwalByBioskop(bioskop.getIdBioskop());
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "cannot delete", "cannot delete data");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

    }

    public void deleteStudio() {
        try {
            XStream x = new XStream();
            Studio s = (Studio) x.fromXML(x.toXML(selectedStudio));
            new StudioDAO().delete(s);
            FacesMessage msg = new FacesMessage("delete", "successfull delete data");
            FacesContext.getCurrentInstance().addMessage(null, msg);
            listStudio = new StudioDAO().getAllStudioByBioskop(bioskop);
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "cannot delete", "cannot delete data");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

    }

    public void disableStudio() {
        XStream x = new XStream();
        Studio s = (Studio) x.fromXML(x.toXML(selectedStudio));
        s.setStatus(!s.isStatus());
        new StudioDAO().update(s);

        listStudio.clear();
        listStudio = new StudioDAO().getAllStudioByBioskop(bioskop);
        FacesMessage msg = new FacesMessage("success", "successfull");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void disableSchedule() {
        Jadwal j = new JadwalDAO().getById(idJadwal);
        j.setStatus(!j.isStatus());

        XStream x = new XStream();
        Jadwal jdwl = (Jadwal) x.fromXML(x.toXML(j));
        new JadwalDAO().update(jdwl);

        listJadwal = new JadwalDAO().getJadwalByBioskop(id);
        FacesMessage msg = new FacesMessage("success", "successfull");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}