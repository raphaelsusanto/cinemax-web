/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cineplex.bean;

import com.cineplex.dao.BioskopDAO;
import com.cineplex.dao.StudioDAO;
import com.cineplex.entity.Bioskop;
import com.cineplex.entity.MapSeat;
import com.cineplex.entity.Studio;
import com.thoughtworks.xstream.XStream;
import java.io.Serializable;
import java.util.ArrayList;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author raphael
 */
@SessionScoped
@ManagedBean
public class AddStudioBean implements Serializable {

    private Bioskop bioskop;
    private Studio studio;
    private ArrayList<MapSeat> mapSeat;
    private int row = 5;
    private int col = 5;


    public AddStudioBean() {
        try {
            Integer id = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
            bioskop = new BioskopDAO().getById(id);
            studio= new Studio();
            mapSeat = new ArrayList<MapSeat>();
        } catch (Exception e) {
        }
    }
    
    public boolean refresh(){
         try {
            Integer id = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
            bioskop = new BioskopDAO().getById(id);
        } catch (Exception e) {
        }
         return true;
    }

    public Bioskop getBioskop() {
        return bioskop;
    }

    public void setBioskop(Bioskop bioskop) {
        this.bioskop = bioskop;
    }

    public Studio getStudio() {
        return studio;
    }

    public void setStudio(Studio studio) {
        this.studio = studio;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public ArrayList<MapSeat> getMapSeat() {
        return mapSeat;
    }

    public void setMapSeat(ArrayList<MapSeat> mapSeat) {
        this.mapSeat = mapSeat;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void change() {
        mapSeat.clear();
        int r = row + 64;
        for (int i = 0; i < row * col; i++) {
            mapSeat.add(new MapSeat(true, ((char) r) + "" + ((i % col) + 1) + ""));
            if (((i + 1) % col) == 0 && i != 0) {
                r--;
            }
        }
    }

    public String save() {
        MapSeat mapSeatArray[][] = new MapSeat[row][col];
        int count = 0;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                MapSeat m = mapSeat.get(count);
                mapSeatArray[i][j] = new MapSeat(m.isStatus(), m.getText());
                count++;
            }
        }
        studio.setBioskop(bioskop);
        studio.setDenah(new XStream().toXML(mapSeatArray));
        studio.setStatus(true);
        Studio s = (Studio) new XStream().fromXML(new XStream().toXML(studio));
        new StudioDAO().save(s);

        FacesMessage msg = new FacesMessage("success", "");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        mapSeat.clear();
        studio= new Studio();
        return "DetailBioskop";
    }
}
