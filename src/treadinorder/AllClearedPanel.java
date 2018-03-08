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
import treadinorder.eventlistener.ACPListener;
import treadinorder.eventlistener.ACPToTitleLabelListener;

public class AllClearedPanel extends JFXPanel {
	// インスタンス変数
	private JLabel clearedLabel;	// クリアラベル
	private JLabel scoreLabel;		// スコアラベル
	private JLabel toTitleLabel;	// タイトル画面へ戻るラベル
	
	public AllClearedPanel(MainPanel mPanel, int score) {
		// BoxLayoutに設定
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		// デフォルトフォントを作成
		Font font = new Font(MainPanel.GEN_FONTNAME, Font.PLAIN, mPanel.getHeight() * 7 / 100);
		// テロップフォントを作成
		Font telopFont = null;
		try {
			telopFont = Font.createFont(Font.TRUETYPE_FONT, new File(MainPanel.TELOPFONT_PATH));
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
		
		 // クリアラベルを設定
		clearedLabel = new JLabel("DUTY FULFILLED");
		clearedLabel.setFont(telopFont.deriveFont(Font.PLAIN, mPanel.getHeight() * 8 / 100));
		clearedLabel.setForeground(new Color(0xE0D77F));
		clearedLabel.setSize(clearedLabel.getPreferredSize());
		clearedLabel.setAlignmentX(CENTER_ALIGNMENT);
		
		// スコアラベルを設定
		scoreLabel = new JLabel("スコア：　" + score);
		scoreLabel.setFont(font);
		scoreLabel.setSize(scoreLabel.getPreferredSize());
		scoreLabel.setAlignmentX(CENTER_ALIGNMENT);
		
		// タイトルへ戻るラベルを設定
		toTitleLabel = new JLabel("タイトル画面へ戻る（Spaceキー）");
		toTitleLabel.setFont(font.deriveFont(Font.PLAIN, mPanel.getHeight() * 7 / 100));
		toTitleLabel.setSize(scoreLabel.getPreferredSize());
		toTitleLabel.setAlignmentX(CENTER_ALIGNMENT);
		
		// リスナーをコンポーネントに追加
		toTitleLabel.addMouseListener(new ACPToTitleLabelListener(mPanel, toTitleLabel));
		ACPListener acpListener = new ACPListener(mPanel, this);
		addAncestorListener(acpListener);
		addKeyListener(acpListener);
		
		// コンポーネントをパネルに追加
		add(Box.createVerticalGlue());
		add(clearedLabel);
		add(Box.createVerticalStrut(mPanel.getHeight() * 5 / 100));
		add(scoreLabel);
		add(Box.createVerticalStrut(mPanel.getHeight() / 10));
		add(toTitleLabel);
		add(Box.createVerticalGlue());
		
		// クリアサウンドを再生
		Media media = new Media(new File(ClearedPanel.CLEARED_SOUND_PATH).toURI().toString());
		MediaPlayer player = new MediaPlayer(media);
		player.play();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		
		// 文字背景を黒く塗る
		g2.fillRect(0, clearedLabel.getY(), getWidth(), clearedLabel.getHeight());
	}
}
