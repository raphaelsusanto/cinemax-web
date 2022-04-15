/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cineplex.service;

import com.cineplex.dao.FilmDAO;
import com.cineplex.dao.MemberDAO;
import com.cineplex.entity.Film;
import com.cineplex.entity.Member;
import com.thoughtworks.xstream.XStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author raphael
 */
public class FilmService {

    public String nowPlaying(String email) {
        FilmDAO dao = new FilmDAO();
        List<Film> data = dao.getNowPlaying();
        List<Film> data2 = new ArrayList<Film>();
        for (Film f : data) {
            f.setJadwals(null);
            data2.add(f);
        }


        MemberDAO memberDAO = new MemberDAO();
        Member m = memberDAO.getById(email);
        Calendar thisTime = Calendar.getInstance();
        Calendar birthday = Calendar.getInstance();
        birthday.setTime(m.getBirthday());

        if (thisTime.get(Calendar.YEAR) - birthday.get(Calendar.YEAR) < 17) {
            for (int i = 0; i < data2.size(); i++) {
                if (data2.get(i).getPemirsa().equalsIgnoreCase("D")) {
                    data2.remove(i);
                }
            }
        }

        return new XStream().toXML(data2);
    }

    public String commingSoon() {
        FilmDAO dao = new FilmDAO();
        List<Film> data = dao.getCommingSoon();
        List<Film> data2 = new ArrayList<Film>();
        for (Film f : data) {
            f.setJadwals(null);
            data2.add(f);
        }
        return new XStream().toXML(data2);
    }

    public String getFilmById(int id) {
        FilmDAO dao = new FilmDAO();
        Film f = dao.getById(id);
        f.setJadwals(null);
        return new XStream().toXML(f);
    }

    public String nowPlayingByBioskop(int id, String email) {
        FilmDAO dao = new FilmDAO();
        List<Film> data = dao.getNowPlayingByBioskop(id);
        for (Film film : data) {
            film.setJadwals(null);
        }

        MemberDAO memberDAO = new MemberDAO();
        Member m = memberDAO.getById(email);
        Calendar thisTime = Calendar.getInstance();
        Calendar birthday = Calendar.getInstance();
        birthday.setTime(m.getBirthday());

        if (thisTime.get(Calendar.YEAR) - birthday.get(Calendar.YEAR) < 17) {
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).getPemirsa().equalsIgnoreCase("D")) {
                    data.remove(i);
                }
            }
        }

        return new XStream().toXML(data);
    }

    public static void main(String[] args) {
    }
}
