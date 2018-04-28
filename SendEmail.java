
//package project;
//https://myaccount.google.com/lesssecureapps?pli=1
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import java.util.*;

public class SendEmail
{
    public int mail(int account,String email,String type,String amount,String bal,String path)
    {
		final String username="info.gamepr@gmail.com";
		final String pass="justgaming";
		
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		Session session = Session.getInstance(props,new javax.mail.Authenticator()
		{
                        @Override
			protected PasswordAuthentication getPasswordAuthentication()
			{
				return new PasswordAuthentication(username,pass);
				
			}
		});
		
		try{
			
                        Message message = new MimeMessage(session);
                        message.setFrom(new InternetAddress("info.gamepr@gmail.com"));
                        message.setRecipients(Message.RecipientType.TO,
                        InternetAddress.parse(email));
                        message.setSubject("Account Details");
                        //message.setContent("<h:body style=background);
                        if(path=="")
                        {
                        Multipart mp=new MimeMultipart();
                        BodyPart msgbp=new MimeBodyPart();
                        BodyPart msgbp1=new MimeBodyPart();
                        BodyPart msgbp2=new MimeBodyPart();
                        BodyPart msgbp3=new MimeBodyPart();
                        
                        msgbp.setText("Account Number:"+account);
                        msgbp1.setText("\nTransection Type:"+type);
                        msgbp2.setText("Transection Amount:"+amount);
                        msgbp3.setText("Available Balance:"+bal);
                        mp.addBodyPart(msgbp);
                        mp.addBodyPart(msgbp1);
                        mp.addBodyPart(msgbp2);
                        mp.addBodyPart(msgbp3);
                        message.setContent(mp);
                        }
                        else
                        {
                        Multipart mp=new MimeMultipart();
                        BodyPart msgbp=new MimeBodyPart();
                        BodyPart msgbp1=new MimeBodyPart();
                        BodyPart msgbp3=new MimeBodyPart();
                        BodyPart msgbp4=new MimeBodyPart();
                        BodyPart msgbpf=new MimeBodyPart();
                        msgbp.setText("Account Number:"+account);
                        msgbp1.setText("\nTransection Type:"+type);
                        msgbp3.setText("Available Balance:"+bal);
                        msgbp4.setText("This Mail contain Transaction File");
                        String fname=path;
                        DataSource src=new FileDataSource(fname);
                        msgbpf.setDataHandler(new DataHandler(src));
                        msgbpf.setFileName(fname);
                        mp.addBodyPart(msgbp4);
                        mp.addBodyPart(msgbp);
                        mp.addBodyPart(msgbp1);
                        mp.addBodyPart(msgbp3);  
                        mp.addBodyPart(msgbp);
                        mp.addBodyPart(msgbpf);
                        message.setContent(mp);
                        }
                        Transport.send(message);
                        return 1;
                        
				} 
		catch(MessagingException e)
		{
			System.out.println(e);
			return 0;
		}
    }
}
