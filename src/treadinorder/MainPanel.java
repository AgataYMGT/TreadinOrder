package treadinorder;

import java.awt.GridLayout;

import javax.swing.JPanel;

public class MainPanel extends JPanel implements Runnable {
	private StartPanel sPanel;
	private GamePanel gPanel;
	private GameOverPanel goPanel;
	
	public MainPanel() {		
		// グリッド(行1, 縦1)を作成
		setLayout(new GridLayout(1, 1));
	}
	
	public void run() {
		
	}
}