/**
 * StartpPanelのJLabel用マウスリスナー
 */

package treadinorder.eventlistener;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;

import treadinorder.MainPanel;

public class SpLabelListener implements MouseListener {
	private JLabel label;
	private MainPanel mPanel;
	
	public SpLabelListener(JLabel label, MainPanel mPanel) {
		this.label = label;
		this.mPanel = mPanel;
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// スタートラベルがクリックされるとステートを1に変更
		mPanel.setState(1);
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// 文字を青に
		label.setForeground(Color.BLUE);
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// 文字を黒に
		label.setForeground(Color.BLACK);
	}
	
	public void mousePressed(MouseEvent arg0) {}
	public void mouseReleased(MouseEvent arg0) {}
	
}
