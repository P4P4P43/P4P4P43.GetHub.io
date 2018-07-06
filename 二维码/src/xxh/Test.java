package xxh;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.imageio.ImageIO;

import com.swetake.util.Qrcode;


public class Test {
	public static void main(String args[]) throws Exception{
		int size=163;
		int version=9;
		
		Qrcode qrcode =new Qrcode();
		String s = " ";
		qrcode.setQrcodeVersion(version);
		qrcode.setQrcodeErrorCorrect('H');
		qrcode.setQrcodeEncodeMode('B');
		
		byte[] data = s.getBytes("utf-8");
		boolean[][] qrdata = qrcode.calQrcode(data);
		
		BufferedImage bufferedImage = new BufferedImage(size,size,BufferedImage.TYPE_INT_RGB);
		
		Graphics2D gs = bufferedImage.createGraphics();
		
		gs.setBackground(Color.WHITE);
		gs.setColor(Color.BLACK);
		
		gs.clearRect(0, 0, size, size);
		
		int startR=0,startG=0,startB=255;
		int endR=255,endG=0,endB=0;
		int pixoff = 2;
		for(int i=0;i<qrdata.length;i++){
			for(int j=0;j<qrdata.length;j++){
				if(qrdata[i][j]){
					int r = startR + (endR - startR) * (i+j)/2/ qrdata.length;
					int g = startG + (endG - startG) * (i+j)/2/ qrdata.length;
					int b = startB + (endB - startB) * (i+j)/2/ qrdata.length;
					Color color = new Color(r,g,b);
					gs.setColor(color);
					gs.fillRect(3*i+pixoff, 3*j+pixoff, 3, 3);
				}
			}
		}
		BufferedImage logo=scale("D:/logo2.png",60,60,true);
		
		int o = (size-logo.getWidth())/2;
		gs.drawImage(logo,o,o,60,60,null);
		gs.dispose();
		bufferedImage.flush();
		try {
			ImageIO.write(bufferedImage, "png", new File("D:/qrcode.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}System.out.println("OK");
	}

	private static BufferedImage scale(String logoPath, int width, int height, boolean hasFiller) throws IOException {
		// TODO Auto-generated method stub
		
		double ratio=0.0;
		File file = new File(logoPath);
		BufferedImage srcImage =ImageIO.read(file);
		Image destImage = srcImage.getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH);
		
		if((srcImage.getHeight()>height)||srcImage.getWidth()>width){
			if(srcImage.getHeight()>srcImage.getWidth()){
				ratio = (new Integer(height)).doubleValue()/srcImage.getHeight();
			}else{
				ratio = (new Integer(width)).doubleValue()/srcImage.getWidth();
			}
			AffineTransformOp op =new AffineTransformOp(AffineTransform.getScaleInstance(ratio,ratio),null);
			destImage = op.filter(srcImage, null);
		}
		 
		if(hasFiller){
			BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
			Graphics2D graphic = image.createGraphics();
			graphic.setColor(Color.white);
			graphic.fillRect(0, 0, width, height);
			if(width== destImage.getWidth(null)){
				graphic.drawImage(destImage, 0, (height-destImage.getHeight(null))/2, destImage.getWidth(null),
						destImage.getHeight(null),Color.white,null);			
			}else{
				graphic.drawImage(destImage,(width-destImage.getWidth(null))/2, 0,destImage.getWidth(null),
						destImage.getHeight(null),Color.white,null);			
			}
			graphic.dispose();
			destImage=image;
		}
		return (BufferedImage) destImage;
	}

}