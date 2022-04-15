/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cineplex.bean;

import com.cineplex.dao.FilmDAO;
import com.cineplex.entity.Film;
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
public class ManageFilmBean implements Serializable {

    private List<Film> dataFilm;
    private Film selectedFilm;

    public ManageFilmBean() {
        dataFilm = new FilmDAO().getAll();
    }

    public Film getSelectedFilm() {
        return selectedFilm;
    }

    public void setSelectedFilm(Film selectedFilm) {
        this.selectedFilm = selectedFilm;
    }

    public List<Film> getDataFilm() {
        return dataFilm;
    }

    public void setDataFilm(List<Film> dataFilm) {
        this.dataFilm = dataFilm;
    }

    public void delete() {

        try {
            XStream x = new XStream();
            Film f = (Film) x.fromXML(x.toXML(selectedFilm));
            new FilmDAO().delete(f);
            dataFilm = new FilmDAO().getAll();

            FacesMessage msg = new FacesMessage("success", "Success delete Film");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (Exception e) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Failed", "Cannot delete film data");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

    }
}
