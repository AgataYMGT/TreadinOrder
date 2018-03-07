package treadinorder;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;

import javafx.embed.swing.JFXPanel;
import treadinorder.eventlistener.GOPListener;
import treadinorder.eventlistener.ToTitleLabelListener;

public class GameOverPanel extends JFXPanel {
	// インスタンス変数
	private JLabel gameOverLabel;	// ゲームオーバーラベル
	private JLabel scoreLabel;		// スコアラベル
	private JLabel toTitleLabel;	// タイトル画面に戻るラベル
	
	public GameOverPanel(MainPanel mPanel, int score) {
		// BoxLayoutに設定
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		// 共通設定
		Font font = new Font(MainPanel.GEN_FONTNAME, Font.PLAIN, mPanel.getHeight() * 8 / 100);
		
		// ゲームオーバーラベルを設定
		gameOverLabel = new JLabel("YOU DIED");
		gameOverLabel.setFont(font);
		gameOverLabel.setSize(gameOverLabel.getPreferredSize());
		gameOverLabel.setForeground(new Color(0xBB0206));		// 暗い赤
		gameOverLabel.setAlignmentX(CENTER_ALIGNMENT);
		
		// スコアラベルを設定
		scoreLabel = new JLabel("スコア：　" + score);
		scoreLabel.setFont(font);
		scoreLabel.setSize(scoreLabel.getPreferredSize());
		scoreLabel.setAlignmentX(CENTER_ALIGNMENT);
		
		// タイトルへ戻るラベルを設定
		toTitleLabel = new JLabel("タイトル画面へ戻る（Spaceキー）");
		toTitleLabel.setFont(font);
		toTitleLabel.setSize(toTitleLabel.getPreferredSize());
		toTitleLabel.setAlignmentX(CENTER_ALIGNMENT);
		
		// リスナーをコンポーネントに追加
		toTitleLabel.addMouseListener(new ToTitleLabelListener(mPanel, toTitleLabel));
		GOPListener gopListener = new GOPListener(mPanel, this);
		addKeyListener(gopListener);
		addAncestorListener(gopListener);
		
		// コンポーネントをパネルに追加
		add(Box.createVerticalGlue());
		add(gameOverLabel);
		add(Box.createVerticalStrut(mPanel.getHeight() * 8 / 100));
		add(scoreLabel);
		add(Box.createVerticalStrut(mPanel.getHeight() / 10));
		add(toTitleLabel);
		add(Box.createVerticalGlue());
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		
		// ゲームオーバーラベルの文字背景を黒く塗る
		g2.fillRect(0, gameOverLabel.getY(), getWidth(), gameOverLabel.getHeight());
	}
}
