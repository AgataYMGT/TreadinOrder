package treadinorder.eventlistener;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;

import treadinorder.MainPanel;

public class ToTitleLabelListener implements MouseListener {
	// インスタンス変数
	private MainPanel mPanel;		// メインパネル
	private JLabel toTitleLabel;	// タイトル画面へ戻るパネル
	
	public ToTitleLabelListener(MainPanel mPanel, JLabel toTitleLabel) {
		this.mPanel = mPanel;
		this.toTitleLabel = toTitleLabel;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// クリックされるとスタートパネルに遷移する
		mPanel.switchStartPanel();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// カーソルが乗ると文字を青く
		toTitleLabel.setForeground(Color.BLUE);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// カーソルが離れると文字を黒く
		toTitleLabel.setForeground(Color.BLACK);
	}

	public void mousePressed(MouseEvent arg0) {}
	public void mouseReleased(MouseEvent arg0) {}
}
