package treadinorder;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;

import treadinorder.eventlistener.GPAncestorListener;
import treadinorder.eventlistener.GPKeyListener;

import static treadinorder.TOUtils.horizonalCentering;
import static treadinorder.TOUtils.verticalCentering;

public class GamePanel extends JPanel implements Runnable {
	// クラス変数
	public static final int[] DIFFICULTY = {5, 7, 9, 11};	// 難易度
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
	private Random random = new Random();	// ランダムクラス
	
	// コンポーネント
	private TilePanel tilePanel;	// タイルパネル
	private JLabel playerLabel;		// プレイヤーラベル
	private Box onesetBox;			//　指定される順番のワンセットボックス
	private Box orderNumBox;			// ワンセットの順番を表示するボックス
	
	private Image[] tileImages;		// タイル画像の配列
	
	private int oneset;			// 指定される順番のワンセット
	private int difficulty;		// 難易度
	private int tileDrawsize;	// タイルの描画サイズ
	
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
		// レイアウトマネージャーを停止
		this.setLayout(null);
		
		// 難易度を設定
		difficulty = DIFFICULTY[1];
		
		// タイルパネルを生成
		tilePanel = new TilePanel(this, difficulty);
		
		// タイル画像を取得する
		tileImages = tilePanel.getOnesetTileImages();
		
		// ワンセットを取得する
		oneset = tilePanel.getOneset();
		// タイルの描画サイズを取得する
		tileDrawsize = tilePanel.getTileDrawsize();
		
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
				JLabel onesetTileLabel = ImageLabel.getScaledImageJLabel(tileImages[i], tileDrawsize, tileDrawsize);
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
				JLabel onesetTileLabel = ImageLabel.getScaledImageJLabel(tileImages[i], tileDrawsize, tileDrawsize);
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
			
			tilePanel.repaint();
			playerLabel.repaint();
			
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
	
	public JPanel getTilePanel() {
		return tilePanel;
	}
}
