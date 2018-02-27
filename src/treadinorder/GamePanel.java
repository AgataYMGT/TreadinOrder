package treadinorder;

import java.awt.Font;
import java.awt.Graphics;
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
import javax.swing.Box;
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
	
	// コンポーネント
	private JPanel tilePanel;	// タイルパネル
	private JLabel playerLabel;	// プレイヤーラベル
	private Box onesetBox;		//　踏む順番のワンセットボックス
	private Box orderNumBox;	// ワンセットの順番を表示するボックス
	
	int[][] map;						// 迷路マップ
	private final int mapSize;	// マップの全体サイズ
	
	private int playerSpeed;		// プレイヤースピード
	
	// 壁
	private int wallWidth, wallHeight;		// 壁の大きさ
	private int leftWallX, leftWallY;		// 左の壁の座標
	private int rightWallX, rightWallY;	// 右の壁の座標
	
	// 十字キー押下フラグ
	private boolean[] pressedCrossKey = {false, false, false, false};
	
	private Thread th;		// スレッド
		
	public GamePanel(MainPanel mPanel) {
		// パネルサイズを設定
		this.setSize(mPanel.getSize());
		
		// インスタンス変数の初期化
		this.random = new Random();
		mapSize = (int)(getHeight() * 0.75);
		
		this.setLayout(null);
		
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
			tilePanel = new JPanel(new GridLayout(difficulty, difficulty));
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
				// 画像の縦横比率
				double drawRatio = 0.8;
				int playerWidth = (int)(playerImage.getWidth() * tileDrawsize * drawRatio / playerImage.getHeight());
				int playerHeight = (int)(tileDrawsize * drawRatio);
				playerLabel = ImageLabel.getScaledImageJLabel(playerImage, playerWidth, playerHeight);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			// プレイヤーのスピードを設定
			playerSpeed = 2;
			
			// ワンセットのボックスと順番表示ボックスを作成
			onesetBox = Box.createVerticalBox();
			orderNumBox = Box.createVerticalBox();
			for(int i = 0; i < oneset; i++) {
				try {
					JLabel onesetTileLabel = ImageLabel.getScaledImageJLabel(onesetTiles[i], tileDrawsize, tileDrawsize);
					onesetBox.add(onesetTileLabel, 0);
				} catch (IOException e) {
					e.printStackTrace();
				}
				JLabel orderNumLabel = new JLabel(String.valueOf(i + 1));
				orderNumLabel.setFont(new Font(MainPanel.GEN_FONTNAME, Font.PLAIN, 100));
				orderNumBox.add(orderNumLabel, 0);
			}
			for(int i = 0; i < 2; i++) {
				try {
					JLabel onesetTileLabel = ImageLabel.getScaledImageJLabel(onesetTiles[i], tileDrawsize, tileDrawsize);
					onesetBox.add(onesetTileLabel, 0);
				} catch (IOException e) {
					e.printStackTrace();
				}
				JLabel orderNumLabel = new JLabel(String.valueOf(i + 1));
				orderNumLabel.setFont(new Font(MainPanel.GEN_FONTNAME, Font.PLAIN, 100));
				orderNumBox.add(orderNumLabel, 0);
			}
			
			// コンポーネントサイズを設定
			playerLabel.setSize(playerLabel.getPreferredSize());
			tilePanel.setSize(tilePanel.getPreferredSize());
			onesetBox.setSize(onesetBox.getPreferredSize());
			orderNumBox.setSize(orderNumBox.getPreferredSize());
			
			// コンポーネント位置を設定
			tilePanel.setLocation(horizonalCentering(this.getWidth(), tilePanel.getWidth()), verticalCentering(this.getHeight(), tilePanel.getHeight()));
			playerLabel.setLocation(horizonalCentering(this.getWidth(), playerLabel.getWidth()), tilePanel.getY() + tilePanel.getHeight() + 5);
			
			// 壁の大きさを設定
			wallWidth = tileDrawsize;
			wallHeight = getHeight();
			leftWallX = tilePanel.getX() - wallWidth;
			leftWallY = 0;
			rightWallX = tilePanel.getX() + tilePanel.getWidth();
			rightWallY = 0;
			
			// ワンセットボックスの位置を設定
			int onesetBoxX = horizonalCentering(leftWallX, onesetBox.getWidth()) + rightWallX + wallWidth;
			int onesetBoxY = verticalCentering(this.getHeight(), onesetBox.getHeight());
			onesetBox.setLocation(onesetBoxX, onesetBoxY);
			// 順番表示ボックスの設定
			orderNumBox.setLocation(onesetBoxX - orderNumBox.getWidth() - 10, onesetBoxY);
			
			// リスナーを追加
			this.addKeyListener(new GPKeyListener(this));
			// パネルが可視/不可視になると呼ばれるリスナー
			this.addAncestorListener(new GPAncestorListener(this));
			
			// コンポーネントをこのパネルに追加
			this.add(playerLabel);
			this.add(tilePanel);
			this.add(onesetBox);
			this.add(orderNumBox);
			
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
	
	/**
	 * 親に対して中央揃えにするためのX座標を返す
	 * @param x 中央揃えにしたいオブジェクトの横幅
	 * @return 中央揃えのためのX座標
	 */
	public int horizonalCentering(int parent, int width) {
		return (parent - width) / 2;
	}
	
	/**
	 * 親に対して縦方向の中央揃えにするためのY座標を返す
	 * @param y 縦中央揃えにしたいオブジェクトの縦幅
	 * @return 縦中央揃えのためのY座標
	 */
	public int verticalCentering(int parent, int height) {
		return (parent - height) / 2;
	}
	
	@Override
	public void run() {
		while(true) {
			// 十字キーが押下されていれば、その方向にプレイヤーを移動させる
			for(int i = 0; i < pressedCrossKey.length; i++) {
				if( pressedCrossKey[i] ) {
					drawPlayer(playerSpeed, VECTOR[i][0], VECTOR[i][1]);
				}
			}
			
			// プレイヤーが壁の外に出ないようにする
			int playerX = playerLabel.getX();
			int playerWidth = playerLabel.getWidth();
			if(playerX < leftWallX + wallWidth) {
				playerLabel.setLocation(leftWallX + wallWidth, playerLabel.getY());
			} else if(playerX + playerWidth > rightWallX) {
				playerLabel.setLocation(rightWallX - playerWidth, playerLabel.getY());
			}
			
			try {
				Thread.sleep(16);	// 16ms待機（60fps）
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// 左の壁を描画
		g.fillRect(leftWallX, leftWallY, wallWidth, wallHeight);
		// 右の壁を描画
		g.fillRect(rightWallX, rightWallY, wallWidth, wallHeight);
	}
	
	public void setPressedKey(int key) {
		pressedCrossKey[key] = true;
	}
	
	public void setReleasedKey(int key) {
		pressedCrossKey[key] = false;
	}
}
