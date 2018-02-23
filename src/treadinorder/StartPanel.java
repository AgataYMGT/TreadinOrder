/**
 * スタート画面パネル
 */

package treadinorder;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import treadinorder.eventlistener.SpKeyListener;
import treadinorder.eventlistener.SpLabelListener;

public class StartPanel extends JPanel {	
	private static enum Dists {
		debian, ubuntu, linuxmint, redhat, fedora, centos
	};
	
	// ロゴ
	private JLabel logo;
	private JLabel[] distLogos;
	
	// フォントとラベル
	private Font genFont;
	private JLabel descriLabel;
	private JLabel beginLabel;
	
	public StartPanel(MainPanel mp) {
		// BoxLayoutに設定
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		// 各画像の読み込み
		distLogos = new JLabel[Dists.values().length];
		try {
			// ロゴの読み込み
			BufferedImage image = ImageIO.read(this.getClass().getResource("assets/TreadinOrder.png"));
			logo = new JLabel(new ImageIcon(image));
			// ディストリの数だけロゴを読み込む
			short i = 0;
			for(Dists dist : Dists.values()) {
				image = ImageIO.read(this.getClass().getResource("assets/" + dist.name() + "400.png"));
				distLogos[i] = new JLabel(new ImageIcon(image.getScaledInstance(image.getWidth() / 2, image.getHeight() / 2, Image.SCALE_AREA_AVERAGING)));
				i++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// 全体フォントの設定
		genFont = new Font(MainPanel.GEN_FONTNAME, Font.PLAIN, 48);
				
		// 説明ラベルの設定
		descriLabel = new JLabel("<html>制限時間内に指示された<br>順番でタイルの上を歩け!</html>");
		descriLabel.setFont(genFont);
		descriLabel.setForeground(new Color(0xbd0d0d));
		descriLabel.setSize(descriLabel.getPreferredSize());
		descriLabel.setHorizontalAlignment(JLabel.CENTER);
		
		// ゲームスタートラベルの設定
		beginLabel = new JLabel("ゲームスタート（Spaceキー）");
		beginLabel.setFont(genFont);
		beginLabel.setSize(beginLabel.getPreferredSize());
		beginLabel.setHorizontalAlignment(JLabel.CENTER);
		
		// リスナーを各コンポーネントに追加
		beginLabel.addMouseListener(new SpLabelListener(beginLabel, mp));
		this.addKeyListener(new SpKeyListener(mp));
		
		// ディストリロゴ用のFlowLayoutパネル作成
		JPanel distPanel = new JPanel(new FlowLayout());
		for(int i = 0; i < distLogos.length; i++) {
			distPanel.add(distLogos[i]);
		}
		distPanel.setMaximumSize(distPanel.getPreferredSize());
		
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
		this.add(distPanel);
		this.add(Box.createGlue());
		this.add(beginLabel);
		this.add(Box.createGlue());
	}
}