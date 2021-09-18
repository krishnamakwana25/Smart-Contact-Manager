package com.smartcontactmanager.service;
import java.io.File;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.springframework.stereotype.Service;
@Service
public class EmailService 
{
	public boolean sendEmail(String subject, String message, String to)
	{
		boolean flag = false;
		//variable for gmail
		String host = "smtp.gmail.com";
		String from = "smartcontactmanager.2021@gmail.com";		
		//get the system properties
		//get the system properties 
		Properties properties = System.getProperties();
		System.out.println(properties);
		 //setting important information to properties object
		  
		 //host set 
		 properties.put("mail.smtp.host",host);
		 properties.put("mail.smtp.port",465);
		 properties.put("mail.smtp.ssl.enable","true");
		 properties.put("mail.smtp.auth","true");
		  
		 //step:1 to get the session object
		 Session session = Session.getInstance(properties, new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				// TODO Auto-generated method stub
				return new PasswordAuthentication("smartcontactmanager.2021@gmail.com","scm#2021");
			}
				
		 }); 
		 session.setDebug(true);
		 
		 //step:2 compose the message [text, mutli media]
		 MimeMessage mimeMessage = new MimeMessage(session);
		 try 
		 {
			 //from mail
			 mimeMessage.setFrom(from);
			 
			 //adding recipient
			 mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			 
			 //adding subject to message
			 mimeMessage.setSubject(subject);
			 
			 //adding text to message 
			// mimeMessage.setText(message);
			 mimeMessage.setContent(message,"text/html");
//			 mimeMessage.setContent("<h1>Hello Disha..!</h1>","text/html");
//			 mimeMessage.setContent("<h3>How are you ?</h3><h4>Hope you are fine. <br>I am your good wisher.Even I need of Fresher who works very efficiently<br> Are you interested in learning Web developement and sales force work if yes then send me message.</h4><br><h4>Thanks & Regards</h4>","text/html");
				/*
				 * mimeMessage.setContent("","text/html");
				 * mimeMessage.setContent("<h4>Thanks & Regards</h4>","text/html");
				 * mimeMessage.setContent("<a href='#'>Thanks & Regards</a>","text/html");
				 */
			 //step:3 send the message using transport class
			 Transport.send(mimeMessage);
			 
			 System.out.println("send message successfully..");
			 flag=true;
		 }
		 catch (Exception e) 
		 {
			e.printStackTrace();
		}
		 return flag ;
	}
	
	
	//THIS METHOD IS ONLY FOR SEDING ATTACHMENT
	private static void sendAttachment(String message, String subject, String to, String from) {
		// //variable for gmail
		String host = "smtp.gmail.com";
		
		//get the system properties 
		Properties properties = System.getProperties();
		System.out.println(properties);
		 //setting important information to properties object
		  
		 //host set 
		 properties.put("mail.smtp.host",host);
		 properties.put("mail.smtp.port",465);
		 properties.put("mail.smtp.ssl.enable","true");
		 properties.put("mail.smtp.auth","true");
		  
		 //step:1 to get the session object
		 Session session = Session.getInstance(properties, new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				// TODO Auto-generated method stub
				return new PasswordAuthentication("smartcontactmanager.2021@gmail.com","scm#2021");
			}
				
		 }); 
		 session.setDebug(true);
		 
		 //step:2 compose the message [text, multi media]
		 MimeMessage mimeMessage = new MimeMessage(session);
		 try 
		 {
			 //from mail
			 mimeMessage.setFrom(from);
			 
			 //adding recipient
			 mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			 
			 //adding subject to message
			 mimeMessage.setSubject(subject);
			 
			 //adding attachment to message 
			 String path = "C:\\Users\\Krishna\\Desktop\\GID-6.pdf";
//			 String path = "H:\\PHOTOS\\COLLEGE\\24-7-18\\IMG_20180724_161749_HDR.jpg";
			 
			 MimeMultipart part = new MimeMultipart();
			
			 MimeBodyPart fortext = new MimeBodyPart();
			 MimeBodyPart forfile = new MimeBodyPart();
			 
			 try 
			 {
				 fortext.setText(message);
				 
				 File file = new File(path);
				 forfile.attachFile(file);
				 part.addBodyPart(fortext);
				 part.addBodyPart(forfile);
			 }
			 catch (Exception e) 
			 {
				 e.printStackTrace();
			 }
			 
			 mimeMessage.setContent(part);
			 
			 //step:3 send the message using transport class
			 Transport.send(mimeMessage);
			 
			 System.out.println("send message successfully..");
		 }
		 catch (Exception e) 
		 {
			e.printStackTrace();
		}
	}
}