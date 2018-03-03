package treadinorder.eventlistener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import treadinorder.LevelShowPanel;
import treadinorder.MainPanel;

public class SpKeyListener implements KeyListener {
	private MainPanel mPanel;
	
	public SpKeyListener(MainPanel mPanel) {
		this.mPanel = mPanel;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		// Spaceキー(コード:32)が押下されたらステートを1に変更
		if(e.getKeyCode() == 32) {
			mPanel.switchLevelShowPanel(LevelShowPanel.EASY);
		}
	}

	public void keyReleased(KeyEvent arg0) {}
	public void keyTyped(KeyEvent arg0) {}

}
