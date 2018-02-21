/**
 * StartpPanelのJLabel用マウスリスナー
 */

package treadinorder.eventlistener;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Icon;
import javax.swing.JLabel;

public class SpLabelListener implements MouseListener {
	private JLabel label;
	
	public SpLabelListener(JLabel label) {
		this.label = label;
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		
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
