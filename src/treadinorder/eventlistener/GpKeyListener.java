package treadinorder.eventlistener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import treadinorder.GamePanel;

public class GpKeyListener implements KeyListener {
	// インスタンス変数
	private GamePanel gPanel;	// ゲームパネルクラス
	
	public GpKeyListener(GamePanel gPanel) {
		this.gPanel = gPanel;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		
	}

	public void keyReleased(KeyEvent arg0) {}
	public void keyTyped(KeyEvent arg0) {}
}
