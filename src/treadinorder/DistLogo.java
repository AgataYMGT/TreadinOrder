package treadinorder;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

public class DistLogo extends JComponent {
	private BufferedImage image;
	
	public DistLogo(URL imgPath) {
		try {
			// 画像を読み込む
			this.image = ImageIO.read(imgPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// コンポーネントのサイズを画像に合わせる
		this.setSize(image.getWidth(), image.getHeight());
	}
	
	@Override
	public void paintComponent(Graphics g) {
		// 画像の描画
		g.drawImage(image, 0, 0, null);
	}
	
	/**
	 * BufferedImageのリサイズを行う
	 * @param width 画像の横幅
	 * @param height 画像の縦幅
	 * @return リサイズされた画像
	 */
	public void setScale(int width, int height) {
		// コンポーネントのサイズを画像に合わせる
		this.setSize(width, height);
		// 画像のスケールを変えて描画し直す
		image.getGraphics().drawImage(image.getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING), 0, 0, width, height, null);
	}
}
