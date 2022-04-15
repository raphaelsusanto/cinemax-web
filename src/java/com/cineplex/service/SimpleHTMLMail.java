package com.cineplex.service;


import javax.mail.*;
import javax.mail.internet.*;

import java.util.Properties;

public class SimpleHTMLMail {

    String username = null;
    String password = null;

    /**
     * Creates a new instance of SendMail
     */
    public void postMail(String recipients[], String subject, String message, String from, String pwd) throws MessagingException {
        boolean debug = false;

        //Set the host smtp address  
        Properties props = new Properties();

        //props.put("mail.smtp.host", "smtp.live.com");  

        String host = "smtp.gmail.com";
        props.put("mail.smtps.auth", "true");

        // Authenticator a1= new PopupAuthenticator();  

        // create some properties and get the default Session  
        Session session = Session.getDefaultInstance(props, null);
        session.setDebug(debug);

        // create a message  
        Message msg = new MimeMessage(session);

        // set the from and to address  
        //InternetAddress addressFrom = new InternetAddress(from);  
        //msg.setFrom(addressFrom);  

        InternetAddress[] addressTo = new InternetAddress[recipients.length];
        for (int i = 0; i < recipients.length; i++) {
            addressTo[i] = new InternetAddress(recipients[i]);
        }
        msg.setRecipients(Message.RecipientType.TO, addressTo);

        // Setting the Subject and Content Type  
        msg.setSubject(subject);
        msg.setContent(message, "text/html; charset=ISO-8859-1");

        //msg.setText(message);  
        //Transport.send(msg);   

        username = from;
        password = pwd;

        Transport t = session.getTransport("smtps");
        try {
            t.connect(host, username, password);
            //t.connect(host, username);  
            t.sendMessage(msg, msg.getAllRecipients());
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            t.close();
        }

    }
   
}