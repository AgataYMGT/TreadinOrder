/*
 * スタート画面パネル
 */

package treadinorder;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class StartPanel extends JPanel {
	private static enum Dists {
		debian, ubuntu, linuxmint, redhat, fedora, centos
	};
	private final float distDrawWidth[] = {-2.5f, -1.5f, -0.5f, 0.5f, 1.5f, 2.5f};
	
	private BufferedImage logo;
	private BufferedImage[] distLogos;
	
	private MainPanel mp;
	
	public StartPanel(MainPanel mp) {
		this.mp = mp;
		
		// レイアウトマネージャーを停止
		this.setLayout(null);
		
		// 各画像の読み込み
		distLogos = new BufferedImage[Dists.values().length];
		try {
			// ロゴの読み込み
			logo = ImageIO.read(this.getClass().getResource("assets/TreadinOrder.png"));
			// ディストリの数だけ読み込み
			short cnt = 0;
			for(Dists dist : Dists.values()) {
				BufferedImage distLogo = ImageIO.read(this.getClass().getResource("assets/" + dist.name() + "400.png"));
				// 読み込んだ画像を縮小して新たなオブジェクトを生成
				distLogos[cnt] = biResize(distLogo, distLogo.getWidth() / 2, distLogo.getWidth() / 2);
				cnt++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		// アンチエイリアス処理を有効に
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		int drawWidth, drawHeight;
		// ロゴの描画
		drawWidth = xToDrawCenter(logo.getWidth());
		drawHeight = (int)(this.getHeight() * 0.15);
		g2.drawImage(logo, drawWidth, drawHeight, logo.getWidth(), logo.getHeight(), this);
		
		// 各ディストリのロゴの描画
		int distDrawSize;
		drawHeight = (int)(this.getHeight() * 0.25);
		for(int i = 0; i < Dists.values().length; i++) {
			distDrawSize = distLogos[i].getWidth();
			drawWidth = (int)(xToDrawCenter(distDrawSize) + distDrawSize * distDrawWidth[i]);
			g2.drawImage(distLogos[i], drawWidth, drawHeight, distDrawSize, distDrawSize, this);
		}
	}
	
	/**
	 * BufferedImageをリサイズして新たにBufferedImageを作成
	 * @param image
	 * @param width
	 * @param height
	 * @return
	 */
	public static BufferedImage biResize(BufferedImage image, int width, int height) {
		BufferedImage bi = new BufferedImage(width, height, image.getType());
		bi.getGraphics().drawImage(image.getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING), 0, 0, width, height, null);
		return bi;
	}
	
	/**
	 * 画像を中央に表示するためのX座標取得
	 * @param imageSize
	 * @return 中央表示のためのX座標
	 */
	public int xToDrawCenter(int imageX) {
		return (this.getWidth() - imageX) / 2;
	}
}