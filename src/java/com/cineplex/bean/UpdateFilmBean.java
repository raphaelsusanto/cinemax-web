/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cineplex.bean;

import com.cineplex.dao.FilmDAO;
import com.cineplex.entity.Film;
import com.thoughtworks.xstream.XStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import org.primefaces.model.DefaultUploadedFile;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author raphael
 */
@RequestScoped
@ManagedBean
public class UpdateFilmBean implements Serializable {

    private static Film film;
    private UploadedFile file;

    public UpdateFilmBean() {
        try {
            Integer id = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id"));
            film = new FilmDAO().getById(id);
            file = new DefaultUploadedFile();
        } catch (Exception e) {
        }
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public String updateFilm() {
        if (file != null) {
            film.setGambar(file.getFileName());
            imgSave(file.getContents(), file.getFileName());
        }
        XStream x = new XStream();
        Film f = (Film) x.fromXML(x.toXML(film));
        new FilmDAO().update(f);
        film = null;
        FacesMessage msg = new FacesMessage("success", "successfull update data");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        return "ManageFilm";
    }

    public void imgSave(byte[] readData, String dosyaisim) {
        File f = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("") + "/img/" + dosyaisim);
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(readData);
            fos.flush();
            fos.close();
        } catch (Exception ex) {
            ex.printStackTrace();
//Logger.getLogger(bean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
