package treadinorder.eventlistener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import treadinorder.GameOverPanel;
import treadinorder.MainPanel;

public class GOPListener implements AncestorListener, KeyListener {
	// インスタンス変数
	private MainPanel mPanel;
	private GameOverPanel goPanel;
	
	public GOPListener(MainPanel mPanel, GameOverPanel goPanel) {
		this.mPanel = mPanel;
		this.goPanel = goPanel;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		// スペースキーが押下されるとスタート画面へ遷移する
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			mPanel.switchStartPanel();
		}
	}

	public void keyReleased(KeyEvent arg0) {}
	public void keyTyped(KeyEvent arg0) {}

	@Override
	public void ancestorAdded(AncestorEvent e) {
		// パネルが可視化されるとフォーカス要求をする
		goPanel.requestFocusInWindow();
	}
	
	public void ancestorMoved(AncestorEvent arg0) {}
	public void ancestorRemoved(AncestorEvent arg0) {}

}
