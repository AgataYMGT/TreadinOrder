/**
 * 各パネル制御用のメインパネル
 */

package treadinorder;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;

public class MainPanel extends JPanel implements Runnable {
	public int panelWidth, panelHeight;
	
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
		panelWidth = panelSize.width;
		panelHeight = panelSize.height;
		
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
					this.removeAll();
					gPanel = new GamePanel(this);
					this.add(gPanel);
					break;
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
	
	/**
	 * ゲームステートのセッター
	 * @param state ゲームステート
	 */
	public void setState(int state) {
		this.state = state;
	}
}
