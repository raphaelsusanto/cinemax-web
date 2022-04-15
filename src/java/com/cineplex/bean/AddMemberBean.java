/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cineplex.bean;

import com.cineplex.dao.MemberDAO;
import com.cineplex.entity.Member;
import com.cineplex.entity.Saldo;
import com.cineplex.service.SimpleHTMLMail;
import com.thoughtworks.xstream.XStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.mail.MessagingException;

/**
 *
 * @author raphael
 */
@ViewScoped
@ManagedBean
public class AddMemberBean implements Serializable {

    private Member newMember;
    private String gender;

    public AddMemberBean() {
        newMember = new Member();
        gender = "m";
    }

    public Member getNewMember() {
        return newMember;
    }

    public void setNewMember(Member newMember) {
        this.newMember = newMember;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void save() {

        final String pass = newMember.getEmail().getBytes().toString().substring(3);
        newMember.setGender(gender.equalsIgnoreCase("m"));
        newMember.setRole(Boolean.FALSE);
        newMember.setPassword(pass);
        Saldo s = new Saldo(newMember, 50000, new Date());
        Set<Saldo> list = new HashSet<Saldo>();
        list.add(s);
        newMember.setSaldos(list);

        XStream x = new XStream();
        Member m = (Member) x.fromXML(x.toXML(newMember));
        try {
            new MemberDAO().save(m, 50000);

            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        //http://192.168.43.120:8084/21WebApp/faces/app/21MobileApp.apk
                        String body="Your Password <b>" + pass + "</b><br/> <a href='http://192.168.43.120:8084/21WebApp/faces/app/21MobileApp.apk'>Download Aplikasi Mobile</a>";
                        new SimpleHTMLMail().postMail(new String[]{newMember.getEmail()}, "Registration Cinemax Mobile", body, "raphael.susanto@gmail.com", "kmzwa8awaa*");
                    } catch (MessagingException ex) {
                        Logger.getLogger(AddMemberBean.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }).start();

            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("./ManageMember.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(AddMemberBean.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (Exception e) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN,"Failed! Username already exists! ", "username already exists");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }
}
