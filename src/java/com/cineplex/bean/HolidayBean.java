/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cineplex.bean;

import com.cineplex.dao.HolidayDAO;
import com.cineplex.entity.Holiday;
import java.io.Serializable;
import java.util.Date;
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
public class HolidayBean implements Serializable{

    private Date selectedDate;

    public HolidayBean() {
        selectedDate = new Date();
    }

    public Date getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(Date selectedDate) {
        this.selectedDate = selectedDate;
    }

    public void save() {
        try {
            new HolidayDAO().save(new Holiday(selectedDate));
            FacesMessage msg = new FacesMessage("success add " + selectedDate, "");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (Exception e) {

            FacesMessage msg = new FacesMessage("Failed","");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

    }
}
