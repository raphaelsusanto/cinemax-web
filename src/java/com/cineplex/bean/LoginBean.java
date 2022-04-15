/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cineplex.bean;

import com.cineplex.dao.MemberDAO;
import com.cineplex.entity.Member;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.eclipse.jdt.core.dom.ThisExpression;

/**
 *
 * @author raphael
 */
@SessionScoped
@ManagedBean
public class LoginBean {

    public Member currentMember;
    private String email;
    private String password;

    public LoginBean() {
    }

    public Member getCurrentMember() {
        return currentMember;
    }

    public void setCurrentMember(Member currentMember) {
        this.currentMember = currentMember;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return email;
    }

    public void setUsername(String username) {
        this.email = username;
    }
    
   

    public String login() {
        MemberDAO dao = new MemberDAO();
        Boolean result = dao.cekLogin(email, password);
        Member m = null;

        String page = "";

        if (result) {
            m = dao.getById(email);
            currentMember = m;
            if (currentMember != null) {
                if (currentMember.getRole()) {
                } else {
                    page = "Member";
                }
            }
        } 
        else {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed", "Username or Password Wrong!");
            FacesContext.getCurrentInstance().addMessage(null, msg);
//            FacesMessage msg = new FacesMessage("Welcome " + m.getEmail(), "successfull Login");
//            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        return result ? page : "Login";
    }

    public boolean validate() {
        try {
            if (currentMember == null) {
                FacesContext.getCurrentInstance().getExternalContext().redirect("./Login.xhtml");
            }
        } catch (IOException ex) {
            Logger.getLogger(LoginBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    public String logout() {
        currentMember = null;
        return "Login";
    }

    public boolean validate2() {
        try {
            if (currentMember != null) {
                if (currentMember.getRole()) {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("./ManageBioskop.xhtml");
                } else {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("./Member.xhtml");
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(LoginBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println(new LoginBean().login());
//        new LoginBean().login();
    }
}
