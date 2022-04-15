<%@page import="com.cineplex.service.MemberService"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.cineplex.service.PesananService"%>
<%@page import="com.cineplex.service.StudioService"%>
<%@page import="com.cineplex.service.JadwalService"%>
<%@page import="com.cineplex.service.BioskopService"%>
<%@page import="com.cineplex.service.FilmService"%>
<%@page import="com.cineplex.service.LoginService"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<% if (request.getParameter("do").equals("login")) {
        String u = request.getParameter("u");
        String p = request.getParameter("p");
        LoginService s = new LoginService();
        out.write(s.login(u, p).toString());
    } else if (request.getParameter("do").equals("nowplaying")) {
        String email=request.getParameter("email");
        out.write(new FilmService().nowPlaying(email));
    } else if (request.getParameter("do").equals("commingsoon")) {
        out.write(new FilmService().commingSoon());
    } else if (request.getParameter("do").equals("nowPlayingByBioskop")) {
        int id = Integer.parseInt(request.getParameter("id"));
        String email = request.getParameter("email");
        out.write(new FilmService().nowPlayingByBioskop(id,email));
    } else if (request.getParameter("do").equals("getFilm")) {
        int id = Integer.parseInt(request.getParameter("id"));
        out.write(new FilmService().getFilmById(id));
    } else if (request.getParameter("do").equals("getAllBioskop")) {
        out.write(new BioskopService().getAll());
    } else if (request.getParameter("do").equals("getBioskop")) {
        int id = Integer.parseInt(request.getParameter("id"));
        out.write(new BioskopService().getBioskopById(id));
    } else if (request.getParameter("do").equals("getAllBioskopByFilm")) {
        int id = Integer.parseInt(request.getParameter("id"));
        out.write(new BioskopService().getAllByFilm(id));
    } else if (request.getParameter("do").equals("getJadwal")) {
        int idFilm = Integer.parseInt(request.getParameter("idFilm"));
        int idBioskop = Integer.parseInt(request.getParameter("idBioskop"));
        String date=request.getParameter("date");
        out.write(new JadwalService().getJadwal(idFilm, idBioskop, date));
    } else if (request.getParameter("do").equals("getJam")) {
        int idFilm = Integer.parseInt(request.getParameter("idFilm"));
        int idBioskop = Integer.parseInt(request.getParameter("idBioskop"));
        String date=request.getParameter("date");
        out.write(new JadwalService().getJam(idFilm, idBioskop,date));
    } else if (request.getParameter("do").equals("getStudioByJadwal")) {
        int id = Integer.parseInt(request.getParameter("id"));
        out.write(new StudioService().getStudioByJadwal(id));
    } else if (request.getParameter("do").equals("getNotAvaibleSeat")) {
        int id = Integer.parseInt(request.getParameter("id"));
        String jam = request.getParameter("jam");
        String tgl = request.getParameter("date");
        out.write(new StudioService().getNotAvaibleSeat(id, jam,tgl));
    } else if (request.getParameter("do").equals("pesan")) {
        int id = Integer.parseInt(request.getParameter("idJadwal"));
        String username = request.getParameter("username");
        String jam = request.getParameter("jam");
        String no = request.getParameter("no");
        String[] noKursi = no.split(",");
        String date=request.getParameter("date");
        out.write(new PesananService().pesan(id, username, noKursi, jam,date));
    } else if (request.getParameter("do").equals("detailPesan")) {
        int id = Integer.parseInt(request.getParameter("idJadwal"));
        String date=request.getParameter("date");
        out.write(new PesananService().getDetailOrder(id,date));
    } else if (request.getParameter("do").equals("getMember")) {
        String id = request.getParameter("id");
        out.write(new MemberService().getMemberById(id));
    }else if (request.getParameter("do").equals("getSaldo")) {
        String id = request.getParameter("id");
        out.write(new MemberService().getSaldo(id));
    } else if (request.getParameter("do").equals("changePassword")) {
        String id = request.getParameter("id");
        String old = request.getParameter("old");
        String newPass = request.getParameter("new");
        out.write(new MemberService().changePassword(id, old, newPass));
    }else if (request.getParameter("do").equals("getOrderHistory")) {
        String id = request.getParameter("id");
        out.write(new PesananService().getOrderHistory(id));
    }else if (request.getParameter("do").equals("nothing")) {
        response.sendRedirect("./faces/Login.xhtml");
    }else if (request.getParameter("do").equals("getDetailHistoryOrder")) {
        String id = request.getParameter("id");
        out.write(new PesananService().getDetailHistoryOrder(id));
    }else if (request.getParameter("do").equals("getAllDate")) {
        int idFilm = Integer.parseInt(request.getParameter("idFilm"));
        int idBioskop = Integer.parseInt(request.getParameter("idBioskop"));
        out.write(new JadwalService().getAllDate(idFilm, idBioskop));
    }%>
