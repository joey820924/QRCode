import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;

import javax.imageio.ImageIO;  
  
import jp.sourceforge.qrcode.QRCodeDecoder;  
import jp.sourceforge.qrcode.data.QRCodeImage;  
import jp.sourceforge.qrcode.exception.DecodingFailedException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


class ConnectSQL{
	String DB;
	String user;
	String password;
	Connection con;
	public ConnectSQL(String DB,String user,String password) {
		this.DB = DB;
		this.user = user;
		this.password = password;	
		this.con = null;
	}
	public void check(String key) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("連接成功MySQLToJava");
			con = DriverManager.getConnection("jdbc:mysql://localhost/"+DB+"?user="+user+"&password="+password);
			System.out.println("連接成功MySQL");
			Statement stat = con.createStatement();
			String UPDATE = "UPDATE x.user_test SET state = 1 WHERE EXISTS(SELECT 1 FROM users WHERE email = '"+key+"') AND email = '"+key+"';";
			String CHECK = "SELECT 1 from x.user_test WHERE email = '"+key+"' AND state = 1";
			stat.executeUpdate(UPDATE);
			ResultSet result = stat.executeQuery(CHECK);
			if(result.wasNull()) {
				System.out.println("未到");
			}
			else {
				System.out.println("已到");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				con.close();
			}catch(Exception e) {
				e.printStackTrace();
			}	
		}
	}
}

class J2SEImage implements QRCodeImage {  
    BufferedImage bufImg;  

    public J2SEImage(BufferedImage bufImg) {  
        this.bufImg = bufImg;  
    }  

    public int getWidth() {  
        return bufImg.getWidth();  
    }  

    public int getHeight() {  
        return bufImg.getHeight();  
    }  

    public int getPixel(int x, int y) {  
        return bufImg.getRGB(x, y);  
    }  

}

public class MakeQRCode{
	public static void main(String[]args) throws Exception {
		ConnectSQL con = new ConnectSQL("x","root","joey820924");
		con.check("d22769750@gmail.com");
        String imgPath = "輸入QRCode位址";  
        //String decoderContent = decoderQRCode(imgPath);  
        System.out.println("解析结果如下：");  
        //System.out.println(decoderContent);  
        System.out.println("========decoder success!!!");
	}
	public static void makeQRcode(String data,String filename) {
		ByteArrayOutputStream bout =
                QRCode.from(data)
                        .withSize(250, 250)
                        .to(ImageType.PNG)
                        .stream();
		
		try {
			filename+=".png";
            OutputStream out = new FileOutputStream(filename);
            bout.writeTo(out);
            out.flush();
            out.close();

        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }	
	}
	public static String decoderQRCode(String imgPath) throws IOException {
		File imageFile = new File(imgPath);  
		  
        BufferedImage bufImg = null;  
        String decodedData = null;  
        try {  
            bufImg = ImageIO.read(imageFile);  
  
            QRCodeDecoder decoder = new QRCodeDecoder();  
            decodedData = new String(decoder.decode(new J2SEImage(bufImg)));  
  
        } catch (IOException e) {  
            System.out.println("Error: " + e.getMessage());  
            e.printStackTrace();  
        } catch (DecodingFailedException dfe) {  
            System.out.println("Error: " + dfe.getMessage());  
            dfe.printStackTrace();  
        }  
        return decodedData;
		
	}
	
	
}