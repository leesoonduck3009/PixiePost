package com.example.pixelpost.Utils.SupportClass;

import android.util.Log;
import android.widget.Toast;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

public class EmailSending {
    public static void sendMail(String emailAddresss, String tittle, OnFinishSendingEmailListener listener)
    {
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port","465");
        properties.put("mail.smtp.ssl.enable","true");
        properties.put("mail.smtp.auth","true");
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("nguyen.phucbinh445@gmail.com","wtulpztnyymtmeaz");
            }
        });
        MimeMessage message = new MimeMessage(session);
        try{
            message.addRecipients(Message.RecipientType.TO, emailAddresss);
            message.setSubject(tittle);
            String htmlContent = "<div style=\"font-family: Helvetica,Arial,sans-serif; min-width:1000px; overflow:auto; line-height:2\">\n" +
                    "  <div style=\"margin:50px auto; width:70%; padding:20px 0\">\n" +
                    "    <div style=\"border-bottom:1px solid #eee\">\n" +
                    "      <a href=\"\" style=\"font-size:1.4em; color: linear-gradient(0deg, #D69BEA, #77BAFB); text-decoration:none; font-weight:600\">Pixel Post</a>\n" +
                    "    </div>\n" +
                    "    <p style=\"font-size:1.1em\">Xin chào,</p>\n" +
                    "    <p>Đây là thư tự động gửi vào email. Vui lòng không trả lời thư này.<br> Dưới đây là mã OTP của bạn!</p>\n" +
                    "    <h2 style=\"background: linear-gradient(0deg, #D69BEA, #77BAFB); margin: 0 auto; width: max-content; padding: 0 10px; color: #fff; border-radius: 4px;\">OTP</h2>\n" +
                    "    <p style=\"font-size:0.9em;\">Xin cảm ơn,<br />Pixel Post</p>\n" +
                    "    <hr style=\"border:none; border-top:1px solid #eee\" />\n" +
                    "    <div style=\"float:right; padding:8px 0; color:#aaa; font-size:0.8em; line-height:1; font-weight:300\">\n" +
                    "    </div>\n" +
                    "  </div>\n" +
                    "</div>";

            message.setContent(htmlContent, "text/html; charset=utf-8");
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Transport.send(message);
                    }
                    catch (MessagingException e)
                    {
                        Log.e("email_send", e.getMessage().toString());
                        listener.onFinishSendEmail(e);
                    }
                }
            });
            thread.start();
        }
        catch (MessagingException e)
        {
            Log.e("email_send", e.getMessage().toString());
            listener.onFinishSendEmail(e);
        }
    }
    public interface OnFinishSendingEmailListener{
        void onFinishSendEmail(Exception e);
    }

}
