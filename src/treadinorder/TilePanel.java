package treadinorder;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import treadinorder.TreadinOrder.Tiles;

public class TilePanel extends JPanel {
	// インスタンス変数
	private int difficulty;		// 難易度
	
	private int[][] map;			// 迷路マップ
	private int mapSize;			// マップの大きさ
	
	private int oneset;			// 指定される順番のワンセット
	private int tileDrawsize;	// タイルの描画する大きさ
	
	private Image[] scaledTileImages;	// スケールされたタイル画像配列
	private Image[][] mapTiles;			// 迷路マップの画像版
	
	private final Random random = new Random();	// ランダムクラス
	
	public TilePanel(JPanel parentPanel, int difficulty) {		
		// 難易度を設定
		this.difficulty = difficulty;
		
		// 指定する順番のワンセットをランダムに取得
		oneset = random.nextInt(Tiles.values().length - 2) + 3;
		
		// マップを取得
		map = new Maze(difficulty, difficulty, oneset).getMap();
		// マップサイズを設定
		mapSize = (int)(parentPanel.getHeight() * 0.8);
							
		// タイルを描画する大きさを設定
		tileDrawsize = mapSize / difficulty;
		
		// パネルの推奨サイズを設定
		this.setPreferredSize(new Dimension(tileDrawsize * map.length, tileDrawsize * map[0].length));
		
		// ワンセットのタイルをランダムに決定、画像を取得する
		List<Tiles> tiles = Arrays.asList(Tiles.values());
		Collections.shuffle(tiles);
		
		scaledTileImages = new Image[oneset];
		for(int i = 0; i < oneset; i++) {
			URL imgpath = getClass().getResource("assets/" + tiles.get(i).name() + ".png");
			try {
				BufferedImage image = ImageIO.read(imgpath);
				scaledTileImages[i] = image.getScaledInstance(tileDrawsize, tileDrawsize, Image.SCALE_AREA_AVERAGING);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		// マップをタイル画像に置き換える
		mapTiles = new Image[difficulty][difficulty];
		for(int i = 0; i < map.length; i++) {
			for(int j = 0; j < map[i].length; j++) {
				// その座標がダミーならワンセットからランダムに選び置き換える
				mapTiles[i][j] = scaledTileImages[(map[i][j] == Maze.DUMMY) ? random.nextInt(oneset) : map[i][j]];
			}
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		
		// タイルを敷き詰める
		for(int i = 0; i < map.length; i++) {
			for(int j = 0; j < map[i].length; j++) {
				g2.drawImage(mapTiles[i][j], tileDrawsize * i, tileDrawsize * j, this);
			}
		}
	}
	
	/**
	 * 指定された順番のワンセットの数を返す
	 * @return ワンセットの数
	 */
	public int getOneset() {
		return oneset;
	}
	
	/**
	 * ワンセットの画像の配列を返す
	 * @return ワンセットの画像の配列
	 */
	public Image[] getOnesetTileImages() {
		return scaledTileImages;
	}
	
	/**
	 * このパネルでのタイルの描画サイズを返す
	 * @return　タイルの描画サイズ
	 */
	public int getTileDrawsize() {
		return tileDrawsize;
	}
}
