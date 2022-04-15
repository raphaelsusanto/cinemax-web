/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cineplex.bean;

import com.cineplex.dao.BioskopDAO;
import com.cineplex.entity.Bioskop;
import com.thoughtworks.xstream.XStream;
import java.io.Serializable;
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
public class ManageBioskopBean implements Serializable {

    private List<Bioskop> dataBioskop;
    private Bioskop selectedBioskop;

    public ManageBioskopBean() {
        this.dataBioskop = new BioskopDAO().getAll();
    }

    public List<Bioskop> getDataBioskop() {
        return dataBioskop;
    }

    public void setDataBioskop(List<Bioskop> dataBioskop) {
        this.dataBioskop = dataBioskop;
    }

    public Bioskop getSelectedBioskop() {
        return selectedBioskop;
    }

    public void setSelectedBioskop(Bioskop selectedBioskop) {
        this.selectedBioskop = selectedBioskop;
    }

    public void delete() {
        try {
            XStream x = new XStream();
            Bioskop b = (Bioskop) x.fromXML(x.toXML(selectedBioskop));
            new BioskopDAO().delete(b);
            this.dataBioskop = new BioskopDAO().getAll();
            FacesMessage msg = new FacesMessage("success", "Success delete Bioskop");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Failed", "Cannot delete bioskop");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
}
