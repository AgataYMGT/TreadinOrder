/**
 * 各パネル制御用のメインパネル
 */

package treadinorder;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

public class MainPanel extends JPanel implements Runnable {
	// インスタンス変数	
	public static final String GEN_FONTNAME = "IPAフォント";
	
	// ゲームステート(状態)
	private int state;
	private int oldState;
	
	// パネル
	private StartPanel sPanel;
	private GamePanel gPanel;
	private GameOverPanel goPanel;
	
	// スレッド
	Thread th;
	
	public MainPanel(Dimension panelSize) {
		this.setSize(panelSize);
		
		// グリッド(行1, 縦1)を作成
		this.setLayout(new GridLayout(1, 1));
		
		// 最初にStartPanelを表示するように
		this.sPanel = new StartPanel(this);
		this.add(sPanel);
		
		// スレッドの生成,実行（並列処理開始）
		th = new Thread(this);
		th.start();
	}
	
	public void run() {
		// ステートを監視、値が変わるとパネルを差し替える
		while(true) {
			if(state != oldState) {
				switch(state) {
				case 1:	// 1ならGamePanel
					switchPanel(new LevelShowPanel(new Font(GEN_FONTNAME, Font.PLAIN, 84), LevelShowPanel.EASY));
					try {
						Thread.sleep(3000L);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					switchPanel(new GamePanel(this));
					break;
				case 2:
					removeAll();
					repaint();
				}
			}
			oldState = state;
			revalidate();
			try {
				Thread.sleep(100L);	// 100ms待機
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void switchPanel(JComponent component) {
		removeAll();
		add(component);
		revalidate();
		repaint();
	}
	
	/**
	 * ゲームステートのセッター
	 * @param state ゲームステート
	 */
	public void setState(int state) {
		this.state = state;
	}
}
