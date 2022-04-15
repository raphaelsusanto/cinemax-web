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
public class LoginService {

    public String login(String username, String pass) {
        XStream x = new XStream();
        Boolean result = false;
        try {
            MemberDAO dao = new MemberDAO();

            result = dao.cekLogin(username, pass);
            if (result) {
                Member m = dao.getById(username);

                if (m.getPassword().equals(pass)) {
                    result = true;
                }
            }


            return x.toXML(result);
        } catch (Exception e) {
            return x.toXML(result);
        }

    }

    public static void main(String[] args) {
        LoginService l = new LoginService();
        //l.login("pael", "pael");
        System.out.println(l.login("pal", "pael"));
    }
}
