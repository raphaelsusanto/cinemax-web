/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cineplex.bean;

import com.cineplex.dao.BioskopDAO;
import com.cineplex.entity.Bioskop;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.MapModel;

/**
 *
 * @author raphael
 */
@ManagedBean
public class AddBioskopBean implements Serializable {

    private Bioskop bioskop;
    private MapModel model;
    private String title;
    private double lat;
    private double lng;

    public AddBioskopBean() {
        bioskop = new Bioskop();
        model = new DefaultMapModel();
//        try {
//            int id=Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
//            bioskop=new BioskopDAO().getById(id);
//        } catch (Exception e) {
//        }

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

    public void saveBioskop() {
        new BioskopDAO().save(bioskop);
        bioskop = new Bioskop();

        FacesMessage msg = new FacesMessage("success", "successfull insert data");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public String addMarker() {
        //Marker marker = new Marker(new LatLng(lat, lng), bioskop.getNama());  
        //model.addOverlay(marker);  
        bioskop.setLatitude( lat);
        bioskop.setLongitude(lng);
        new BioskopDAO().save(bioskop);
        bioskop = new Bioskop();

        FacesMessage msg = new FacesMessage("success", "successfull insert data");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        
        return "ManageBioskop";
        // addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Marker Added", "Lat:" + lat + ", Lng:" + lng));  
    }

    public void clear() {
        lat = 0;
        lng = 0;
    }
}
