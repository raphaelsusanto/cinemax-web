/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cineplex.bean;

import com.cineplex.dao.PesananDAO;
import com.cineplex.entity.Pesanan;
import com.cineplex.service.ReportService;
import com.thoughtworks.xstream.XStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author raphael
 */
@ViewScoped
@ManagedBean
public class MemberBean implements Serializable {

    private List<Pesanan> listPesanan = new ArrayList<Pesanan>();
    private Pesanan selectedPesanan;

    public MemberBean() {
        listPesanan = new PesananDAO().getPesananByMember(new LoginBean().getCurrentMember());
    }

    public List<Pesanan> getListPesanan() {
        return listPesanan;
    }

    public void setListPesanan(List<Pesanan> listPesanan) {
        this.listPesanan = listPesanan;
    }

    public Pesanan getSelectedPesanan() {
        return selectedPesanan;
    }

    public void setSelectedPesanan(Pesanan selectedPesanan) {
        this.selectedPesanan = selectedPesanan;
    }

    public void print() throws IOException, JRException {
        XStream x = new XStream();
        Pesanan p = (Pesanan) x.fromXML(x.toXML(selectedPesanan));
        p.setCetak(true);
        new PesananDAO().update(p);
        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("") + "/resources/report/ticket2.jrxml";
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("idPesanan", p.getIdPesanan());
        ReportService reportService = new ReportService(path, param);
      
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        ServletOutputStream outputStream = response.getOutputStream();
        net.sf.jasperreports.engine.JasperExportManager.exportReportToPdfStream(reportService.getReport(), outputStream);
//        try {
//            FacesContext.getCurrentInstance().getExternalContext().redirect("./Member.xhtml");
//        } catch (IOException ex) {
//            Logger.getLogger(MemberBean.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
}
