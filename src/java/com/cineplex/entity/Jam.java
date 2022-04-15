/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cineplex.entity;

/**
 *
 * @author raphael
 */
public class Jam {
    private String jam;
    private int idJadwal;
    private int tmptDudukk;
    private boolean three;

    public int getIdJadwal() {
        return idJadwal;
    }

    public void setIdJadwal(int idJadwal) {
        this.idJadwal = idJadwal;
    }

    public String getJam() {
        return jam;
    }

    public void setJam(String jam) {
        this.jam = jam;
    }

    public boolean isThree() {
        return three;
    }

    public void setThree(boolean three) {
        this.three = three;
    }

    public int getTmptDudukk() {
        return tmptDudukk;
    }

    public void setTmptDudukk(int tmptDudukk) {
        this.tmptDudukk = tmptDudukk;
    }

   
}
