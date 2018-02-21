/**
 * スタート画面パネル
 */

package treadinorder;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;

import treadinorder.eventlistener.SpLabelListener;

public class StartPanel extends JPanel implements Runnable {
	private int panelWidth, panelHeight;
	
	private static enum Dists {
		debian, ubuntu, linuxmint, redhat, fedora, centos
	};
	private final float distDrawWidth[] = {-2.5f, -1.5f, -0.5f, 0.5f, 1.5f, 2.5f};
	
	// ロゴ
	private BufferedImage logo;
	private BufferedImage[] distLogos;
	
	// フォントとラベル
	private Font genFont;
	private JLabel beginLabel;
	
	// スレッド
	private Thread th;
	
	// メインパネル
	private MainPanel mp;
	
	public StartPanel(MainPanel mp) {
		this.mp = mp;
		this.panelWidth = mp.panelWidth;
		this.panelHeight = mp.panelHeight;
		
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
		
		// 全体フォントの設定
		genFont = new Font(MainPanel.GEN_FONTNAME, Font.PLAIN, 48);
				
		// ラベルの設定
		beginLabel = new JLabel("ゲームスタート");
		beginLabel.setFont(genFont);
		beginLabel.setSize(beginLabel.getPreferredSize());
		beginLabel.setBounds(xToDrawCenter(beginLabel.getWidth()), (int)(panelHeight * 0.55), beginLabel.getWidth(), beginLabel.getHeight());
		
		// マウスリスナーに登録
		beginLabel.addMouseListener(new SpLabelListener(beginLabel));
		// パネルにラベルを登録
		this.add(beginLabel);
		
		// スレッドの生成,実行（並列処理開始）
		th = new Thread(this);
		th.start();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		
		int drawWidth, drawHeight;
		// ロゴの描画
		drawWidth = xToDrawCenter(logo.getWidth());
		drawHeight = (int)(panelHeight * 0.15);
		g2.drawImage(logo, drawWidth, drawHeight, logo.getWidth(), logo.getHeight(), this);
		
		// 各ディストリのロゴの描画
		int distDrawSize;
		drawHeight = (int)(panelHeight * 0.3);
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
	 * 中央揃えで描画するためのX座標取得
	 * @param objWidth　オブジェクトの横幅 
	 * @return 中央描画のためのX座標
	 */
	public int xToDrawCenter(int objWidth) {
		return (panelWidth - objWidth) / 2;
	}
	
	// スレッド
	public void run() {
		while(true) {
			repaint();
			try {
				Thread.sleep(100L);	// 100ms待機
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}