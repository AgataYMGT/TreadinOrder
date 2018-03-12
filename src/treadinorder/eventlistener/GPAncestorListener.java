package treadinorder.eventlistener;

import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import treadinorder.GamePanel;

public class GPAncestorListener implements AncestorListener {
	// インスタンス変数
	private GamePanel gPanel;
	
	public GPAncestorListener(GamePanel gPanel) {
		this.gPanel = gPanel;
	}
	
	@Override
	public void ancestorAdded(AncestorEvent e) {
		// 可視化されるとパネルがフォーカス要求をする
		gPanel.requestFocusInWindow();
	}

	public void ancestorMoved(AncestorEvent arg0) {}
	public void ancestorRemoved(AncestorEvent arg0) {}
}
