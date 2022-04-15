/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cineplex.service;

import com.cineplex.dao.MemberDAO;
import com.cineplex.entity.Member;
import com.thoughtworks.xstream.XStream;

/**
 *
 * @author raphael
 */
public class MemberService {
    public String getMemberById(String email){
        MemberDAO dao= new MemberDAO();
        Member m=dao.getById(email);
        m.setPesanans(null);
        m.setSaldos(null);
        return new XStream().toXML(m);
    }
    
    public String changePassword(String username,String old,String newPassword){
        MemberDAO dao= new MemberDAO();
        boolean result=dao.changePassword(username, old, newPassword);
        return new XStream().toXML(result);
    }
    
    public String getSaldo(String username){
        MemberDAO dao= new MemberDAO();
        Long result=dao.getSaldo(username);
        return new XStream().toXML(result);
    }
}
