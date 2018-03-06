package treadinorder.eventlistener;

import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import treadinorder.StartPanel;

public class SPAncestorListener implements AncestorListener {
	// インスタンス変数
	private StartPanel sPanel;
	
	public SPAncestorListener(StartPanel sPanel) {
		this.sPanel = sPanel;
	}

	@Override
	public void ancestorAdded(AncestorEvent e) {
		// 可視化されるとパネルがフォーカス要求をする
		sPanel.requestFocusInWindow();
	}

	public void ancestorMoved(AncestorEvent arg0) {}
	public void ancestorRemoved(AncestorEvent arg0) {}

}
