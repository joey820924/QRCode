import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;
import com.sun.mail.util.MailSSLSocketFactory;

public class sendEmail {
	public static void main(String[]args) {
		String from = "輸入寄件人地址";
		String to = "輸入收件人地址";
		String username = "輸入gmail帳號";
		String password = "輸入gmail密碼";
		String filesrc = "輸入圖片位址";
		String subject = "輸入主旨";
		String content = "輸入Html碼";

		
		sendGmail(from,to,username,password,filesrc,subject,content);
	}
	public static void sendGmail(String from,String to,String username,String password,String filesrc,String subject,String content) {
		try {

			  String host = "smtp.gmail.com";
			  int port = 587;
			  Properties props = new Properties();
			  props.put("mail.smtp.host", host);
			  props.put("mail.smtp.auth", "true");
			  props.put("mail.smtp.starttls.enable", "true");
			  props.put("mail.smtp.port", port);
			  Session session = Session.getInstance(props, new Authenticator() {
			   protected PasswordAuthentication getPasswordAuthentication() {
			    return new PasswordAuthentication(username, password);
			   }
			  });

          // 產生整封 email 的主體 message
          MimeMessage message = new MimeMessage(session);
          message.setFrom(new InternetAddress(from));
          // 設定主旨
          message.setSubject(subject);

          
          MimeBodyPart textPart = new MimeBodyPart();
          StringBuffer html = new StringBuffer();
          html.append(content);
          html.append("<img src='cid:image'/><br>");
          textPart.setContent(html.toString(), "text/html; charset=UTF-8");

          // 圖檔部份，注意 html 用 cid:image，則header要設<image>
          MimeBodyPart picturePart = new MimeBodyPart();
          FileDataSource fds = new FileDataSource(filesrc);
          picturePart.setDataHandler(new DataHandler(fds));
          picturePart.setFileName(fds.getName());
          picturePart.setHeader("Content-ID", "<image>");

          Multipart email = new MimeMultipart();
          email.addBodyPart(textPart);
          email.addBodyPart(picturePart);

          message.setContent(email);
          message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
          Transport transport = session.getTransport("smtp");
          transport.connect(host, port, username, password);
          transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
          transport.close();
      } catch (AddressException e) {
          e.printStackTrace();
      } catch (NoSuchProviderException e) {
          e.printStackTrace();
      } catch (MessagingException e) {
          e.printStackTrace();
      }finally {
      		System.out.println("Completed");
      }
  }
	
}
