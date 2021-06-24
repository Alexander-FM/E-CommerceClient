package com.alexandertutoriales.cliente.ecommerce.utils;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailUtil {
    private static String remitente = "comisariajlope@gmail.com";  //Para la dirección nomcuenta@gmail.com
    private static String clave = "comisariajlo2021";

    public static boolean enviarCorreodeRecuperación(String destinatario) {
        // Esto es lo que va delante de @gmail.com en tu cuenta de correo. Es el remitente también.

        Properties props = System.getProperties();
        props.put("mail.smtp.host", "smtp.gmail.com");  //El servidor SMTP de Google
        props.put("mail.smtp.user", remitente);
        props.put("mail.smtp.clave", clave);    //La clave de la cuenta
        props.put("mail.smtp.auth", "true");    //Usar autenticación mediante usuario y clave
        props.put("mail.smtp.starttls.enable", "true"); //Para conectar de manera segura al servidor SMTP
        props.put("mail.smtp.port", "587"); //El puerto SMTP seguro de Google

        try {

            Session session = Session.getDefaultInstance(props);
            MimeMessage message = new MimeMessage(session);
            message.addHeader("Content-type", "text/html; charset=UTF-8");
            message.setFrom(new InternetAddress(remitente));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));   //Se podrían añadir varios de la misma manera
            message.setSubject("SOLICITUD DE CAMBIO DE CONTRASEÑA");
            message.setText("Hola usuario, para restablecer tu contraseña \n  <a href=\"https://www.youtube.com\">Haz click aqui</a>", "UTF-8", "html");
            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.gmail.com", remitente, clave);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();   //Si se produce un error
            return false;
        }
    }
}
