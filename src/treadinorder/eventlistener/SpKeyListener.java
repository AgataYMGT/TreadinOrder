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
		// Spaceキーが押下されたらゲームを始める
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			mPanel.switchLevelShowPanel(LevelShowPanel.EASY, 0);
		}
	}

	public void keyReleased(KeyEvent arg0) {}
	public void keyTyped(KeyEvent arg0) {}

}
