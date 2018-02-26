package treadinorder.eventlistener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import treadinorder.GamePanel;

public class GPKeyListener implements KeyListener {
	// インスタンス変数
	private GamePanel gPanel;
	
	public GPKeyListener(GamePanel gPanel) {
		this.gPanel = gPanel;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		// キーを押すと十字キーの押下フラグをtrueにする
		switch(e.getKeyCode()) {
		case KeyEvent.VK_UP:
			gPanel.setPressedKey(GamePanel.K_UP);
			break;
		case KeyEvent.VK_LEFT:
			gPanel.setPressedKey(GamePanel.K_LEFT);
			break;
		case KeyEvent.VK_DOWN:
			gPanel.setPressedKey(GamePanel.K_DOWN);
			break;
		case KeyEvent.VK_RIGHT:
			gPanel.setPressedKey(GamePanel.K_RIGHT);
			break;
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		// キーを押すと十字キーの押下フラグをfalseにする
		switch(e.getKeyCode()) {
		case KeyEvent.VK_UP:
			gPanel.setReleasedKey(GamePanel.K_UP);
			break;
		case KeyEvent.VK_LEFT:
			gPanel.setReleasedKey(GamePanel.K_LEFT);
			break;
		case KeyEvent.VK_DOWN:
			gPanel.setReleasedKey(GamePanel.K_DOWN);
			break;
		case KeyEvent.VK_RIGHT:
			gPanel.setReleasedKey(GamePanel.K_RIGHT);
			break;
		}
	}
	public void keyTyped(KeyEvent arg0) {}
}
