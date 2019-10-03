package com.balicamp.webapp.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SendMail {

	protected final Log log = LogFactory.getLog(getClass());

	private Map<String, String> listFile;

	private String subject;

	private String text;

	private String username;

	private String password;

	private String[] toEmail;

	private String smtpHost;

	private String smtpPort;

	private String smtpAuth;

	public String getSmtpHost() {
		return smtpHost;
	}

	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}

	public String getSmtpPort() {
		return smtpPort;
	}

	public void setSmtpPort(String smtpPort) {
		this.smtpPort = smtpPort;
	}

	private boolean isHtml = false;

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean send() {
		boolean isError = false;

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", smtpAuth);
		props.put("mail.smtp.host", smtpHost);
		props.put("mail.smtp.port", smtpPort);

		// props.put("mail.smtp.host", "smtp.sigma.co.id");
		// props.put("mail.smtp.socketFactory.port", "465");
		// props.put("mail.smtp.socketFactory.class",
		// "javax.net.ssl.SSLSocketFactory");
		// props.put("mail.smtp.auth", "true");
		// props.put("mail.smtp.port", "465");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			for (String email : toEmail) {
				message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
			}
			message.setSubject(subject);

			// Create the message part
			BodyPart messageBodyPart = new MimeBodyPart();
			// Fill the message
			if (isHtml)
				messageBodyPart.setContent(text, "text/html");
			else
				messageBodyPart.setText(text);

			// Create a multipar message
			Multipart multipart = new MimeMultipart();
			// Set text message part
			multipart.addBodyPart(messageBodyPart);
			// Part two is attachment
			for (Map.Entry<String, String> entry : listFile.entrySet()) {
				System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
				String fileName = entry.getKey();
				String path = entry.getValue();
				File file = new File(path + "/" + fileName);
				if (file.exists()) {
					messageBodyPart = new MimeBodyPart();
					DataSource source = new FileDataSource(path + "/" + fileName);
					messageBodyPart.setDataHandler(new DataHandler(source));
					messageBodyPart.setFileName(fileName);
					multipart.addBodyPart(messageBodyPart);
				}
			}

			// Send the complete message parts
			message.setContent(multipart);

			System.out.println("Sending Email. Please Wait......");

			Transport.send(message);

			System.out.println("Done");

		} catch (Exception e) {
			isError = true;
			e.printStackTrace();
			log.trace(e);
			System.out.println("Error When Send Email");
		}

		return isError;
	}

	public String[] getToEmail() {
		return toEmail;
	}

	public void setToEmail(String[] toEmail) {
		this.toEmail = toEmail;
	}

	public boolean isHtml() {
		return isHtml;
	}

	public void setHtml(boolean isHtml) {
		this.isHtml = isHtml;
	}

	public Map<String, String> getListFile() {
		return listFile;
	}

	public void setListFile(Map<String, String> listFile) {
		this.listFile = listFile;
	}

	public static void main(String[] args) {
		Map<String, String> file = new HashMap<String, String>();
		file.put("usermanual.pdf", "c:");
		file.put("usermanual2.pdf", "c:");

		String emailList = "prihako.nurukat@gmail.com,prihako.nurukat@sigma.co.id";

		SendMail mail = new SendMail();
		mail.setSubject("Reconcile Daily Report");
		mail.setUsername("nurukat@gmail.com");
		mail.setPassword("prihako01");
		mail.setListFile(file);
		mail.setToEmail(emailList.split(","));
		mail.setSmtpHost("smtp.gmail.com");
		mail.setSmtpPort("587");
		mail.setSmtpAuth("true");
		mail.setText("<h1>Reconcile Report</h1>" +
				"<table border='1'>" +
				"<tr>" + 
					"<th>Status</th>" +
					"<th>Total Invoice</th>" +
					"<th>Total Amount</th>" +
				"</tr>" +
				"<tr>" +
					"<td>Settled</td> " +
					"<td align='right'>10</td> " +
					"<td align='right'>1000</td> " +
				"</tr>" +
				"<tr>" +
					"<td>Settled</td>" +
					"<td align='right'>10</td>" + 
					"<td align='right'>1000</td>" +
				"</tr>" +
				"<tr>" +
					"<td>Settled</td>" + 
					"<td align='right'>10</td>" +
					"<td align='right'>1000</td>" +
				"</tr>" +
			"</table>");
		mail.setHtml(true);

		if (!mail.send()) {
			System.out.println("No Error");
		} else {
			System.out.println("Error");
		}
	}

	public String getSmtpAuth() {
		return smtpAuth;
	}

	public void setSmtpAuth(String smtpAuth) {
		this.smtpAuth = smtpAuth;
	}
}
