package treadinorder;

import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;

import treadinorder.TreadinOrder.Tiles;
import treadinorder.eventlistener.GpKeyListener;

public class GamePanel extends JPanel {
	// クラス変数
	public static final int[] DIFFICULTY = {7};	// 難易度
	
	// インスタンス変数
	Random random;	// ランダムクラス
	int[][] map;		// 迷路マップ
	
	private final int mapSize;
	
	public GamePanel(MainPanel mPanel) {
		this.random = new Random();
		mapSize = (int)(mPanel.panelHeight * 0.8);
		
		for(int difficulty : DIFFICULTY) {
			// 指定する順番のワンセットをランダムに取得
			int oneset = random.nextInt(Tiles.values().length - 2) + 3;
			
			// ワンセットのタイルをランダムに決定、画像を取得する
			List<Tiles> tiles = Arrays.asList(Tiles.values());
			Collections.shuffle(tiles);
			
			BufferedImage[] onesetTiles = new BufferedImage[oneset];
			for(int i = 0; i < oneset; i++) {
				URL imgpath = getClass().getResource("assets/" + tiles.get(i).name() + ".png");
				try {
					onesetTiles[i] = ImageIO.read(imgpath);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			// マップを取得
			map = new Maze(difficulty, difficulty, oneset).getMap();
			
			// タイルを描画する大きさを設定
			int tileDrawsize = mapSize / difficulty;
			
			// パネルを作成してタイルを敷き詰める
			JPanel tilePanel = new JPanel(new GridLayout(difficulty, difficulty));
			for(int i = 0; i < difficulty; i++) {
				for(int j = 0; j < difficulty; j++) {
					// その座標がダミーならワンセットからランダムに選び置き換える
					BufferedImage tileImage = onesetTiles[(map[i][j] == Maze.DUMMY) ? random.nextInt(oneset) : map[i][j]];
					JLabel tileLabel = null;
					try {
						tileLabel = ImageLabel.getScaledImageJLabel(tileImage, tileDrawsize, tileDrawsize);
					} catch (IOException e) {
						e.printStackTrace();
					}
					tilePanel.add(tileLabel);
				}
			}
			// 迷路パネルをこのパネルに追加
			this.add(tilePanel);
			// キーリスナーを追加
			this.addKeyListener(new GpKeyListener(this));
		}
	}
}
