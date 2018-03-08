/**
 * スタート画面パネル
 */

package treadinorder;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import treadinorder.TreadinOrder.Tiles;
import treadinorder.eventlistener.SPAncestorListener;
import treadinorder.eventlistener.SpKeyListener;
import treadinorder.eventlistener.SpLabelListener;

public class StartPanel extends JPanel {	
	// インスタンス変数
	// 画像
	private JLabel logo;				// ロゴ画像
	private JLabel[] tileImage;		// タイル画像
	
	// ラベル
	private JLabel descriLabel;		// 説明ラベル
	private JLabel beginLabel;		// ゲームスタートラベル
	
	public StartPanel(MainPanel mp) {
		// BoxLayoutに設定
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		// 各画像の読み込み
		tileImage = new JLabel[Tiles.values().length];
		BufferedImage image = null;
		try {
			// 画像の読み込み
			image = ImageIO.read(this.getClass().getResource("assets/TreadinOrder.png"));
			logo = ImageLabel.getImageJLabel(image);
			// タイルの数だけその画像を読み込む
			short i = 0;
			for(Tiles tile : Tiles.values()) {
				image = ImageIO.read(this.getClass().getResource("assets/" + tile.name() + ".png"));
				tileImage[i] = ImageLabel.getScaledImageJLabel(image, image.getWidth() / 2, image.getHeight() / 2);
				i++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
				
		// 説明ラベルの設定
		descriLabel = new JLabel("<html>制限時間内に指示された<br>順番でタイルの上を歩け!</html>");
		descriLabel.setFont(MainPanel.DEFAULT_FONT.deriveFont(getHeight() * 4 / 100));
		descriLabel.setForeground(new Color(0xbd0d0d));
		descriLabel.setSize(descriLabel.getPreferredSize());
		descriLabel.setHorizontalAlignment(JLabel.CENTER);
		
		// ゲームスタートラベルの設定
		beginLabel = new JLabel("ゲームスタート（Spaceキー）");
		beginLabel.setFont(MainPanel.DEFAULT_FONT.deriveFont(getHeight() * 4 / 100));
		beginLabel.setSize(beginLabel.getPreferredSize());
		beginLabel.setHorizontalAlignment(JLabel.CENTER);
		
		// リスナーを各コンポーネントに追加
		beginLabel.addMouseListener(new SpLabelListener(beginLabel, mp));
		this.addKeyListener(new SpKeyListener(mp));
		this.addAncestorListener(new SPAncestorListener(this));
		
		// タイル画像用のFlowLayoutパネル作成
		JPanel tilePanel = new JPanel(new FlowLayout());
		for(int i = 0; i < tileImage.length; i++) {
			tilePanel.add(tileImage[i]);
		}
		tilePanel.setMaximumSize(tilePanel.getPreferredSize());
		
		// 各コンポーネントを中心揃え
		logo.setAlignmentX(CENTER_ALIGNMENT);
		descriLabel.setAlignmentX(CENTER_ALIGNMENT);
		beginLabel.setAlignmentX(CENTER_ALIGNMENT);
		
		// 各コンポーネントをパネルに追加
		this.add(Box.createGlue());
		this.add(logo);
		this.add(Box.createRigidArea(new Dimension(0, 20)));
		this.add(descriLabel);
		this.add(Box.createRigidArea(new Dimension(0, 20)));
		this.add(tilePanel);
		this.add(Box.createGlue());
		this.add(beginLabel);
		this.add(Box.createGlue());
	}
}