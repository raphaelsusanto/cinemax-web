/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cineplex.bean;

import com.cineplex.dao.FilmDAO;
import com.cineplex.entity.Film;
import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import org.apache.commons.io.FileUtils;
import org.primefaces.model.DefaultUploadedFile;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author raphael
 */
@ManagedBean
public class AddFilmBean implements Serializable {

    private Film film;
    private UploadedFile file;

    public AddFilmBean() {
        film = new Film();
        file = new DefaultUploadedFile();
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

    public String saveFilm() {
        film.setGambar(file.getFileName());
        new FilmDAO().save(film);
        film= new Film();
        // FileUtils.copyFile(file, file);
        imgSave(file.getContents(), file.getFileName());
        FacesMessage msg = new FacesMessage("success", file.getFileName());
        FacesContext.getCurrentInstance().addMessage(null, msg);
        return "ManageFilm";
    }

    public void imgSave(byte[] readData, String dosyaisim) {
        File f = new File(FacesContext.getCurrentInstance().getExternalContext().getRealPath("")+"/img/"+dosyaisim);
        try {
            if (!f.exists()) {
                f.createNewFile();
            }   
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(readData);
            fos.flush();
            fos.close();
        } catch (Exception ex) {
            System.out.println(f.getAbsolutePath());
            
            ex.printStackTrace();
//Logger.getLogger(bean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
