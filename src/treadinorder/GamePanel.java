package treadinorder;

import java.awt.GridLayout;
import java.awt.Point;
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
import treadinorder.eventlistener.GPAncestorListener;
import treadinorder.eventlistener.GPKeyListener;

public class GamePanel extends JPanel implements Runnable {
	// クラス変数
	public static final int[] DIFFICULTY = {7};	// 難易度
	// プレイヤー画像の相対パス
	public static final String PLAYERIMAGE_PATH = "assets/player.png";
	
	// キー方向
	public static final int K_UP = 0;
	public static final int K_LEFT = 1;
	public static final int K_DOWN = 2;
	public static final int K_RIGHT = 3;
	
	// ベクター
	public static final int[] UP = {0, -1};
	public static final int[] LEFT = {-1, 0};
	public static final int[] DOWN = {0, 1};
	public static final int[] RIGHT = {1, 0};
	public static final int[][] VECTOR = {UP, LEFT, DOWN, RIGHT};
	
	// インスタンス変数
	Random random;	// ランダムクラス
	
	int[][] map;						// 迷路マップ
	private final int mapSize;	// マップの全体サイズ
	
	private JLabel playerLabel;	// プレイヤーラベル
	
	private int playerSpeed;		// プレイヤースピード
	
	// 十字キー押下フラグ
	private boolean[] pressedCrossKey = {false, false, false, false};
	
	private Thread th;		// スレッド
		
	public GamePanel(MainPanel mPanel) {
		this.random = new Random();
		mapSize = (int)(mPanel.panelHeight * 0.8);
		
		this.setLayout(null);
		this.setPreferredSize(mPanel.getSize());
		
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
			
			// プレイヤーラベルを作成
			playerLabel = null;
			try {
				BufferedImage playerImage = ImageIO.read(getClass().getResource(PLAYERIMAGE_PATH));
				playerLabel = ImageLabel.getScaledImageJLabel(playerImage, playerImage.getWidth() * tileDrawsize / playerImage.getHeight(), tileDrawsize);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			// プレイヤーのスピードを設定
			playerSpeed = 1;
			
			// コンポーネントサイズを設定
			playerLabel.setSize(playerLabel.getPreferredSize());
			tilePanel.setSize(tilePanel.getPreferredSize());
			
			// リスナーを追加
			this.addKeyListener(new GPKeyListener(this));
			// パネルが可視/不可視になると呼ばれるリスナー
			this.addAncestorListener(new GPAncestorListener(this));
			
			// コンポーネントをこのパネルに追加
			this.add(playerLabel);
			this.add(tilePanel);
			
			th = new Thread(this);
			th.start();
		}
	}
	
	/**
	 * プレイヤーを描画する
	 * @param vectorX 進む方向のX成分
	 * @param vectorY 進む方向のY成分
	 */
	public void drawPlayer(int speed, int vectorX, int vectorY) {
		Point point = playerLabel.getLocation();
		playerLabel.setLocation(point.x + vectorX + (int)(Math.signum(vectorX) * speed), point.y + vectorY + (int)(Math.signum(vectorY) * speed));
	}
	
	@Override
	public void run() {
		while(true) {
			for(int i = 0; i < pressedCrossKey.length; i++) {
				if( pressedCrossKey[i] ) {
					drawPlayer(playerSpeed, VECTOR[i][0], VECTOR[i][1]);
				}
			}
			
			try {
				Thread.sleep(16);	// 16ms待機（60fps）
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void setPressedKey(int key) {
		pressedCrossKey[key] = true;
	}
	
	public void setReleasedKey(int key) {
		pressedCrossKey[key] = false;
	}
}
