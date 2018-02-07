package treadinorder;

import java.awt.Container;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;

public class TreadinOrder extends JFrame {
	public TreadinOrder() {
		// タイトルを設定
		setTitle("TreadinOrder");
		
		// 全体のパネル制御用パネル(MainPanel)をフレームに追加
		Container contentPane = getContentPane();
		MainPanel panel = new MainPanel();
		contentPane.add(panel);
		
		pack();
		
	}
	
	public static void main(String[] args) {
		TreadinOrder main = new TreadinOrder();
		// フレームの装飾を削除
		main.setUndecorated(true);
		// フルスクリーンにするウィンドウを登録
		GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(main);
		// フレームを閉じた時のデフォルト動作
		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// フレーム表示
		main.setVisible(true);
	}
}
