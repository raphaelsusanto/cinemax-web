/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cineplex.bean;

import com.cineplex.dao.BioskopDAO;
import com.cineplex.entity.Bioskop;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

/**
 *
 * @author raphael
 */
@ManagedBean
public class MapBean implements Serializable{
    private MapModel mapModel;
    private Bioskop selectedBioskop;
    public MapBean() {
        mapModel=new DefaultMapModel();
        String id=FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idBioskop");
        
        selectedBioskop= new BioskopDAO().getById(Integer.parseInt(id));
        
        LatLng coord= new LatLng(selectedBioskop.getLatitude(), selectedBioskop.getLongitude());
        mapModel.addOverlay(new Marker(coord,selectedBioskop.getNama()));
    }

    public MapModel getMapModel() {
        return mapModel;
    }

    public void setMapModel(MapModel mapModel) {
        this.mapModel = mapModel;
    }

    public Bioskop getSelectedBioskop() {
        return selectedBioskop;
    }

    public void setSelectedBioskop(Bioskop selectedBioskop) {
        this.selectedBioskop = selectedBioskop;
    }
    
    public Integer getLatitude(){
        return selectedBioskop.getLatitude().intValue();
    }
    public Integer getLongitude(){
        return selectedBioskop.getLongitude().intValue();
    }
}
