package treadinorder;

import java.awt.Container;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;

public class TreadinOrder extends JFrame {
	// クラス変数
	public static enum Tiles {
		debian, ubuntu, linuxmint, redhat, fedora, centos
	};
	
	// ゲーム素材フォルダの相対パス
	public static final String ASSETS_PATH = "bin/treadinorder/assets/";
	
	public TreadinOrder() {
		// タイトルを設定
		setTitle("TreadinOrder");
		// フレームの装飾を削除
		this.setUndecorated(true);
		// フルスクリーンにするウィンドウを登録
		GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(this);
		// フレームを閉じた時のデフォルト動作
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// 全体のパネル制御用パネル(MainPanel)をフレームに追加
		Container contentPane = getContentPane();
		MainPanel panel = new MainPanel(this.getSize());
		contentPane.add(panel);
	}
	
	public static void main(String[] args) {
		TreadinOrder main = new TreadinOrder();
		// フレーム表示
		main.setVisible(true);
	}
}
