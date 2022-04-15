/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cineplex.service;

import com.cineplex.util.HibernateUtil;
import java.sql.Connection;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author raphael
 */
public class ReportService {

    String path = "";
    HashMap<String, Object> parameter = new HashMap<String, Object>();

    public ReportService(String path, HashMap parameter) {
        this.path = path;
        this.parameter = parameter;
    }

    public JasperPrint getReport() {
        JasperPrint JPrint = null;
        try {
            Connection conn = HibernateUtil.getSessionFactory().openSession().connection();
            JasperReport jprt = JasperCompileManager.compileReport(path);
            JPrint = JasperFillManager.fillReport(jprt, parameter, conn);
        } catch (JRException ex) {
            Logger.getLogger(ReportService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return JPrint;
    }
}
