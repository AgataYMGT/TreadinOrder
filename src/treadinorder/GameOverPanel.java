package treadinorder;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import treadinorder.eventlistener.GOPListener;
import treadinorder.eventlistener.ToTitleLabelListener;

public class GameOverPanel extends JFXPanel {
	// クラス変数
	// 爆発音の相対パス
	public static final String BOMBSOUND_PATH = TreadinOrder.ASSETS_PATH + "sounds/bomb.mp3";
	
	// インスタンス変数
	private JLabel gameOverLabel;	// ゲームオーバーラベル
	private JLabel scoreLabel;		// スコアラベル
	private JLabel toTitleLabel;	// タイトル画面に戻るラベル
	
	public GameOverPanel(MainPanel mPanel, int score) {
		// BoxLayoutに設定
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		// デフォルトフォントを作成
		Font font = new Font(MainPanel.GEN_FONTNAME, Font.PLAIN, mPanel.getHeight() * 7 / 100);
		// テロップフォントを作成
		Font telopFont = null;
		try {
			telopFont = Font.createFont(Font.TRUETYPE_FONT, new File(MainPanel.TELOPFONT_PATH));
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
		
		// ゲームオーバーラベルを設定
		gameOverLabel = new JLabel("YOU DIED");
		gameOverLabel.setFont(telopFont.deriveFont(Font.PLAIN, mPanel.getHeight() * 8 / 100));
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
		toTitleLabel.setFont(font.deriveFont(Font.PLAIN, mPanel.getHeight() * 7 / 100));
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
		add(Box.createVerticalStrut(mPanel.getHeight() * 5 / 100));
		add(scoreLabel);
		add(Box.createVerticalStrut(mPanel.getHeight() / 10));
		add(toTitleLabel);
		add(Box.createVerticalGlue());
		
		// 爆発音を再生
		Media media = new Media(new File(BOMBSOUND_PATH).toURI().toString());
		MediaPlayer player = new MediaPlayer(media);
		player.play();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		
		// ゲームオーバーラベルの文字背景を黒く塗る
		g2.fillRect(0, gameOverLabel.getY(), getWidth(), gameOverLabel.getHeight());
	}
}
