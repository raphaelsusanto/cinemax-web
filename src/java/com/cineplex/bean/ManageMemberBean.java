/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cineplex.bean;

import com.cineplex.dao.MemberDAO;
import com.cineplex.entity.Member;
import com.cineplex.service.PesananService;
import com.cineplex.service.ReportService;
import com.cineplex.service.SimpleHTMLMail;
import com.thoughtworks.xstream.XStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.mail.MessagingException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author raphael
 */
@ViewScoped
@ManagedBean
public class ManageMemberBean implements Serializable {

    private List<Member> listMember;
    private Member selectedMember;
    private int saldo;
    private Long saldoAkhir;

    public ManageMemberBean() {
        listMember = new MemberDAO().getAll();
        saldo = 10000;
    }

    public List<Member> getListMember() {
        return listMember;
    }

    public void setListMember(List<Member> listMember) {
        this.listMember = listMember;
    }

    public Member getSelectedMember() {
        return selectedMember;
    }

    public void setSelectedMember(Member selectedMember) {
        this.selectedMember = selectedMember;
        detailOrder();
    }

    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }

    public void addSaldo() throws IOException, JRException {
        XStream x = new XStream();
        Member m = (Member) x.fromXML(x.toXML(selectedMember));
        new MemberDAO().addSaldo(m, saldo);

        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("") + "/resources/report/nota.jrxml";
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("username", m.getEmail());
        param.put("saldoAkhir", new Long(new MemberDAO().getSaldo(m.getEmail())));
        ReportService reportService = new ReportService(path, param);

        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
        ServletOutputStream outputStream = response.getOutputStream();
        FacesContext.getCurrentInstance().responseComplete();
        net.sf.jasperreports.engine.JasperExportManager.exportReportToPdfStream(reportService.getReport(), outputStream);

    }
    private List<Object[]> data;

    public void detailOrder() {
        PesananService pesananService = new PesananService();
        XStream x = new XStream();
        data = (List<Object[]>) x.fromXML(pesananService.getOrderHistory(selectedMember.getEmail()));
        saldoAkhir = new MemberDAO().getSaldo(selectedMember.getEmail());
    }

    public List<Object[]> getData() {

        List<Object[]> result = new ArrayList<Object[]>();
        try {
            NumberFormat f = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));
            for (Object[] o : data) {
                result.add(new Object[]{o[0], o[1], o[2], f.format((Integer) o[3])});
                //System.out.println(f.format(o[0]));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void setData(List<Object[]> data) {
        this.data = data;
    }

    public Long getSaldoAkhir() {
        return saldoAkhir;
    }

    public void setSaldoAkhir(Long saldoAkhir) {
        this.saldoAkhir = saldoAkhir;
    }

    public void resetPassword() {
        XStream x = new XStream();
        final String pass = selectedMember.getEmail().getBytes().toString().substring(3);
        final Member m = (Member) x.fromXML(x.toXML(selectedMember));
        m.setPassword(pass);
        MemberDAO memberDAO = new MemberDAO();
        memberDAO.update(m);
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    new SimpleHTMLMail().postMail(new String[]{m.getEmail()}, "Reset Password", "Your Password <b>" + pass + "</b>", "raphael.susanto@gmail.com", "kmzwa8awaa*");
                } catch (MessagingException ex) {
                    Logger.getLogger(AddMemberBean.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
    }
}
