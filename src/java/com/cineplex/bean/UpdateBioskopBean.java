/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cineplex.bean;

import com.cineplex.dao.BioskopDAO;
import com.cineplex.entity.Bioskop;
import com.thoughtworks.xstream.XStream;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

/**
 *
 * @author raphael
 */
@ViewScoped
@ManagedBean
public class UpdateBioskopBean implements Serializable {

    private Bioskop bioskop;
    private MapModel model;
    private String title;
    private double lat;
    private double lng;

    public UpdateBioskopBean() {
        try {
            Integer id = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
            bioskop = new BioskopDAO().getById(id);
            model = new DefaultMapModel();
            LatLng coord = new LatLng(bioskop.getLatitude(), bioskop.getLongitude());
            model.addOverlay(new Marker(coord, bioskop.getNama()));
        } catch (Exception e) {
        }


    }

    public Bioskop getBioskop() {
        return bioskop;
    }

    public void setBioskop(Bioskop bioskop) {
        this.bioskop = bioskop;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public MapModel getModel() {
        return model;
    }

    public void setModel(MapModel model) {
        this.model = model;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String update() {
        XStream x = new XStream();

        Bioskop b = (Bioskop) x.fromXML(x.toXML(bioskop));
        b.setLatitude(lat);
        b.setLongitude(lng);
        new BioskopDAO().update(b);
        FacesMessage msg = new FacesMessage("success", "successfull update data");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        return "ManageBioskop";
    }
}
