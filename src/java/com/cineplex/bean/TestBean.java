/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cineplex.bean;

import com.cineplex.entity.MapSeat;
import com.thoughtworks.xstream.XStream;
import java.io.Serializable;
import java.util.ArrayList;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author raphael
 */
@SessionScoped
@ManagedBean
public class TestBean implements Serializable {

    private ArrayList<MapSeat> mapSeat = new ArrayList<MapSeat>();
    private int row = 5;
    private int col = 5;


    public TestBean() {
    }

    public ArrayList<MapSeat> getMapSeat() {
        return mapSeat;
    }

    public void setMapSeat(ArrayList<MapSeat> mapSeat) {
        this.mapSeat = mapSeat;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    
    public void change(){
        mapSeat.clear();
         int r=row+64;
        for (int i = 0; i < row*col; i++) {
            mapSeat.add(new MapSeat(true, ((char)r)+""+((i%col)+1) + ""));
            if (((i+1)%col)==0&&i!=0) {
                r--;
            }
            
        }
    }
    
    public void save(){
         FacesMessage msg = new FacesMessage("success", new XStream().toXML(mapSeat));
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
}
