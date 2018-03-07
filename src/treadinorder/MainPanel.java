/**
 * 各パネル制御用のメインパネル
 */

package treadinorder;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

public class MainPanel extends JPanel {
	// クラス変数	
	public static final String GEN_FONTNAME = "IPAフォント";	// デフォルトフォント
	
	// パネル
	private StartPanel sPanel;
	
	public MainPanel(Dimension panelSize) {
		this.setSize(panelSize);
		
		// グリッド(行1, 縦1)を作成
		this.setLayout(new GridLayout(1, 1));
		
		// 最初にStartPanelを表示するように
		this.add(new StartPanel(this));
	}
	
	/**
	 * パネルを差し替える
	 * @param component	差し替えるコンポーネント
	 */
	private void switchPanel(JComponent component) {
		removeAll();
		add(component);
		revalidate();
		repaint();
	}
	
	/**
	 * スタートパネルに切り替える
	 */
	public void switchStartPanel() {
		switchPanel(sPanel);
	}
	
	/**
	 * 難易度表示パネルに切り替える
	 * 難易度はLevelShowPanelのクラス変数から選択する
	 * @param difficulty	難易度
	 */
	public void switchLevelShowPanel(int difficulty, int score) {
		int countDown = 6;
		switchPanel(new LevelShowPanel(this, new Font(GEN_FONTNAME, Font.PLAIN, 84), difficulty, countDown, score));
	}
	
	/**
	 * ゲームパネルに切り替える
	 * 一辺のタイルの数はGamePanelのクラス変数から難易度として選択する
	 * @param oneSide	一辺のタイルの数
	 */
	public void switchGamePanel(int oneSide, int score) {
		switchPanel(new GamePanel(this, oneSide, score));
	}
	
	/**
	 * クリアパネルに切り替える
	 * 難易度はLevelShowPanelのクラス変数より選択する
	 * @param difficulty	難易度
	 * @param score			スコア
	 */
	public void switchClearedPanel(int difficulty, int score) {
		long showTime = 2000L;
		switchPanel(new ClearedPanel(this, difficulty, score, showTime));
	}
	
	/**
	 * ゲームオーバーパネルに切り替える
	 * @param score	スコア
	 */
	public void switchGameOverPanel(int score) {
		switchPanel(new GameOverPanel());
	}
}
