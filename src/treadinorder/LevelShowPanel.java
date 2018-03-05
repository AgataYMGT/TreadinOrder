package treadinorder;

import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class LevelShowPanel extends JPanel implements Runnable {
	// クラス変数
	// 難易度
	public static final int EASY = 1;
	public static final int NORMAL = 2;
	public static final int HARD = 3;
	public static final int VERY_HARD = 4;
	
	// インスタンス変数
	private MainPanel mPanel;	// メインパネル
	
	private int countDown;			// カウントダウン
	private JLabel countDownLabel; // カウントダウンラベル
	
	private Thread th;		// スレッド
	
	public LevelShowPanel(MainPanel mPanel, Font font, int difficulty, int countDown) {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		this.mPanel = mPanel;
		this.countDown = countDown;
		
		// レベルラベルを作成
		JLabel levelLabel = new JLabel("レベル： " + EASY);
		levelLabel.setFont(font);
		levelLabel.setSize(levelLabel.getPreferredSize());
		
		// 難易度ラベルを表示
		String difStr;
		if(difficulty == EASY) {
			difStr = "EASY";
		} else if(difficulty == NORMAL) {
			difStr = "NORMAL";
		} else if(difficulty == HARD) {
			difStr = "HARD";
		} else if(difficulty == VERY_HARD) {
			difStr = "VERY HARD";
		} else {
			difStr = null;
		}
		JLabel difLabel = new JLabel("難易度： " + difStr);
		difLabel.setFont(font);
		difLabel.setSize(difLabel.getPreferredSize());
		
		// カウンタダウンラベルを設定
		this.countDownLabel = new JLabel();
		countDownLabel.setFont(font);
		countDownLabel.setSize(levelLabel.getPreferredSize());
		
		// コンポーネントを中央揃えに設定
		levelLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		difLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		countDownLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		
		// コンポーネントを追加
		add(Box.createVerticalGlue());
		add(levelLabel);
		add(Box.createVerticalStrut(10));
		add(difLabel);
		add(Box.createVerticalStrut(84));
		add(countDownLabel);
		add(Box.createVerticalGlue());
		
		th = new Thread(this);
		th.start();
	}

	@Override
	public void run() {
		// カウントダウンを行う
		for(int i = countDown - 1; i > -1; i--) {
			countDownLabel.setText("ゲーム開始まで： " + i);
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		// ゲームパネルに切り替える
		mPanel.switchGamePanel(GamePanel.NORMAL);
	}
}
