/*
 * 各パネル制御用のメインパネル
 */

package treadinorder;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;

public class MainPanel extends JPanel implements Runnable {
	public int panelWidth, panelHeight;
	
	public static final String GEN_FONTNAME = "IPAフォント";
	
	private StartPanel sPanel;
	private GamePanel gPanel;
	private GameOverPanel goPanel;
	
	public MainPanel(Dimension panelSize) {
		panelWidth = panelSize.width;
		panelHeight = panelSize.height;
		
		// グリッド(行1, 縦1)を作成
		this.setLayout(new GridLayout(1, 1));
		
		// 最初にStartPanelを表示するように
		this.sPanel = new StartPanel(this);
		this.add(sPanel);
	}
	
	public void run() {
		
	}
}
