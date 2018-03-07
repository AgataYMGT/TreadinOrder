package treadinorder;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class ClearedPanel extends JFXPanel implements Runnable {
	// クラス変数
	public static final String CLEARED_SOUND_PATH = "assets/sounds/cleared.mp3";
	
	// インスタンス変数
	private MainPanel mPanel;
	
	private JLabel clearedLabel;	// クリアラベル
	private JLabel scoreLabel;		// スコアラベル
	
	private int score;				// スコア
	private int difficulty;		// 難易度
	
	private long showTime;		// 表示時間
	
	private Thread th;		// スレッド
	
	public ClearedPanel(MainPanel mPanel, int difficulty, int score, long showTime) {
		this.mPanel = mPanel;
		this.difficulty = difficulty;
		this.score = score;
		this.showTime = showTime;
		
		// BoxLayoutに設定
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		// 共通設定
		Font font = new Font(MainPanel.GEN_FONTNAME, Font.BOLD, mPanel.getHeight() * 8 / 100);
		
		 // クリアラベルを設定
		clearedLabel = new JLabel("YOU CLEARED!");
		clearedLabel.setFont(font);
		clearedLabel.setForeground(new Color(0xE0D77F));
		clearedLabel.setSize(clearedLabel.getPreferredSize());
		clearedLabel.setAlignmentX(CENTER_ALIGNMENT);
		
		// スコアラベルを設定
		scoreLabel = new JLabel("現在のスコア：　" + score);
		scoreLabel.setFont(font);
		scoreLabel.setSize(scoreLabel.getPreferredSize());
		scoreLabel.setAlignmentX(CENTER_ALIGNMENT);
		
		// コンポーネントをパネルに追加
		add(Box.createVerticalGlue());
		add(clearedLabel);
		add(Box.createVerticalStrut(mPanel.getHeight() * 8 / 100));
		add(scoreLabel);
		add(Box.createVerticalGlue());
		
		// スレッドを開始
		th = new Thread(this);
		th.start();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		
		// 文字背景を黒く塗る
		g2.fillRect(0, clearedLabel.getY(), getWidth(), clearedLabel.getHeight());
	}

	@Override
	public void run() {
		// クリアサウンドを再生する
		Media media = new Media(getClass().getResource(CLEARED_SOUND_PATH).toString());
		MediaPlayer player = new MediaPlayer(media);
		player.play();
		
		try {
			Thread.sleep(showTime);	// 指定された時間待機
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// 次の難易度に切り替える
		mPanel.switchLevelShowPanel(difficulty, score);
	}
}
