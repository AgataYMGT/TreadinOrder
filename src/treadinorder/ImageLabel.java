package treadinorder;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class ImageLabel {
	/**
	 * 画像をJLabel（コンポーネント）として返す
	 * @param	 image				元の画像
	 * @return					画像のみで構成されたJLabel
	 * @throws IOException	画像が読み込めなかったとき
	 */
	public static JLabel getImageJLabel(BufferedImage image) throws IOException {
		ImageIcon imgIcon = new ImageIcon(image);
		JLabel label = new JLabel(imgIcon);
		
		return label;
	}
	
	/**
	 * 画像をリサイズしてJLabel（コンポーネント）として返す
	 * @param image				元の画像
	 * @param width				リサイズする横幅
	 * @param height			リサイズする横幅
	 * @return					リサイズされた画像で構成されたJLabel
	 * @throws IOException	画像が読み込めなかったとき
	 */
	public static JLabel getScaledImageJLabel(BufferedImage image, int width, int height) throws IOException {
		ImageIcon imgIcon = new ImageIcon(image.getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING));
		JLabel label = new JLabel(imgIcon);
		
		return label;
	}
}
