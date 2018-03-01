package treadinorder;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
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

public class GamePlayPanel extends JPanel {
	// クラス変数
	// プレイヤー画像の相対パス
	public static final String PLAYERIMAGE_PATH = "assets/player.png";
	
	// インスタンス変数
	private int[][] map;			// 迷路マップ
	private int mapSize;			// マップの大きさ
	
	private int topBottomSpace;	// 上下余白
	
	private int oneset;			// 指定される順番のワンセット
	private int tileDrawsize;	// タイルの描画する大きさ
	
	private Image[] scaledTileImages;	// スケールされたタイル画像配列
	private Image[][] mapTiles;			// 迷路マップの画像版
	
	private int playerWidth, playerHeight;	// プレイヤーの大きさ
	private int playerX, playerY;				// プレイヤーの座標
	
	private Image playerImage;	// プレイヤーの画像

	private final Random random = new Random();	// ランダムクラス
	
	public GamePlayPanel(JPanel parentPanel, int difficulty) {
		// 指定する順番のワンセットをランダムに取得
		oneset = random.nextInt(Tiles.values().length - 2) + 3;
		
		// マップを取得
		map = new Maze(difficulty, difficulty, oneset).getMap();
		// マップサイズを設定
		mapSize = (int)(parentPanel.getHeight() * 0.8);
		
		// 上下余白のサイズを設定
		topBottomSpace = (parentPanel.getHeight() - mapSize) / 2;
		
		// タイルを描画する大きさを設定
		tileDrawsize = mapSize / difficulty;
		
		// パネルの推奨サイズを設定
		this.setPreferredSize(new Dimension(tileDrawsize * map.length, parentPanel.getHeight()));
		
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
		
		// プレイヤー画像を読み込む
		try {
			BufferedImage playerImage = ImageIO.read(getClass().getResource(PLAYERIMAGE_PATH));
			
			// プレイヤーの大きさを設定
			double drawRatio = 0.8;	// タイルに対する描画比率
			playerHeight = (int)(tileDrawsize * drawRatio);
			playerWidth = (int)(playerImage.getWidth() * playerHeight / playerImage.getHeight());
			
			this.playerImage = playerImage.getScaledInstance(playerWidth, playerHeight, Image.SCALE_AREA_AVERAGING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// プレイヤーの初期位置を設定
		playerX = TOUtils.horizonalCentering(mapSize, playerWidth);
		playerY = topBottomSpace + mapSize + 5;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		
		// タイルを敷き詰める
		for(int i = 0; i < map.length; i++) {
			for(int j = 0; j < map[i].length; j++) {
				g2.drawImage(mapTiles[i][j], tileDrawsize * j, topBottomSpace + tileDrawsize * i, this);
			}
		}
		
		// プレイヤーを描画する
		g2.drawImage(playerImage, playerX, playerY, this);
	}
	
	/**
	 * プレイヤーを移動させる
	 * @param speed		プレイヤーの移動速度
	 * @param vectorX	動かすX成分
	 * @param vectorY	動かすY成分
	 */
	public void movePlayer(int speed, int vectorX, int vectorY) {
		playerX += vectorX * speed;
		playerY += vectorY * speed;
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
	
	/**
	 * プレイヤーの座標を返す
	 * @return	プレイヤーの座標
	 */
	public Point getPlayerRelativeLocation() {
		return new Point(playerX, playerY);
	}
	
	/**
	 * プレイヤーの横幅を返す
	 * @return　プレイヤーの横幅
	 */
	public int getPlayerWidth() {
		return playerWidth;
	}
	
	/**
	 * プレイヤーの縦幅を返す
	 * @return プレイヤーの縦幅
	 */
	public int getPlayerHeight() {
		return playerHeight;
	}
}
