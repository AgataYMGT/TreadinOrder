/**
 * スタート画面パネル
 */

package treadinorder;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;

import treadinorder.eventlistener.SpKeyListener;
import treadinorder.eventlistener.SpLabelListener;

public class StartPanel extends JPanel implements Runnable {
	private int panelWidth, panelHeight;
	
	private static enum Dists {
		debian, ubuntu, linuxmint, redhat, fedora, centos
	};
	
	// ロゴ
	private BufferedImage logo;
	private DistLogo[] distLogos;
	
	// フォントとラベル
	private Font genFont;
	private JLabel descriLabel;
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
		distLogos = new DistLogo[Dists.values().length];
		try {
			// ロゴの読み込み
			logo = ImageIO.read(this.getClass().getResource("assets/TreadinOrder.png"));
			// ディストリの数だけロゴを読み込む
			short i = 0;
			for(Dists dist : Dists.values()) {
				// DistLogoインスタンスを作成
				distLogos[i] = new DistLogo(this.getClass().getResource("assets/" + dist.name() + "400.png"));
				// 縦横を0.5倍にする
				distLogos[i].setScale(distLogos[i].getWidth() / 2, distLogos[i].getHeight() / 2);
				i++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// ディストリロゴの描画
		// ボックスを作ってBoxLayoutの配置にする
		Box distBox = Box.createHorizontalBox();
		for(int i = 0; i < distLogos.length; i++) {
			distBox.add(distLogos[i]);
		}
		distBox.setSize(distBox.getPreferredSize());
		distBox.setLocation(xToDrawCenter(distBox.getWidth()), (int)(panelHeight * 0.35));
		
		// 全体フォントの設定
		genFont = new Font(MainPanel.GEN_FONTNAME, Font.PLAIN, 48);
				
		// 説明ラベルの設定
		descriLabel = new JLabel("<html>制限時間内に指示された<br>順番でタイルの上を歩け!</html>");
		descriLabel.setFont(genFont);
		descriLabel.setForeground(new Color(0xbd0d0d));
		descriLabel.setSize(descriLabel.getPreferredSize());
		descriLabel.setHorizontalAlignment(JLabel.CENTER);
		descriLabel.setLocation(xToDrawCenter(descriLabel.getWidth()), (int)(panelHeight * 0.22));
		
		// ゲームスタートラベルの設定
		beginLabel = new JLabel("ゲームスタート（Spaceキー）");
		beginLabel.setFont(genFont);
		beginLabel.setSize(beginLabel.getPreferredSize());
		beginLabel.setLocation(xToDrawCenter(beginLabel.getWidth()), (int)(panelHeight * 0.6));
		
		// リスナーを各コンポーネントに追加
		beginLabel.addMouseListener(new SpLabelListener(beginLabel, mp));
		this.addKeyListener(new SpKeyListener(mp));
		
		// 各コンポーネントをパネルに追加
		this.add(descriLabel);
		this.add(distBox);
		this.add(beginLabel);
		
		// スレッドの生成,実行（並列処理開始）
		th = new Thread(this);
		th.start();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		// 入力フォーカスを取得
		requestFocusInWindow();
		
		int drawWidth, drawHeight;
		// ロゴの描画
		drawWidth = xToDrawCenter(logo.getWidth());
		drawHeight = (int)(panelHeight * 0.1);
		g2.drawImage(logo, drawWidth, drawHeight, logo.getWidth(), logo.getHeight(), this);
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