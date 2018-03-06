package treadinorder;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.io.IOException;
import java.util.Random;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;

import treadinorder.eventlistener.GPAncestorListener;
import treadinorder.eventlistener.GPKeyListener;
import treadinorder.gamepanel.GamePlayPanel;

import static treadinorder.TOUtils.horizonalCentering;
import static treadinorder.TOUtils.verticalCentering;

public class GamePanel extends JPanel implements Runnable {
	// クラス変数
	// 難易度に対する一辺の長さ
	public static final int EASY = 5;
	public static final int NORMAL = 7;
	public static final int HARD = 9;
	public static final int VERY_HARD = 11;
	
	// 難易度ごとのスコア
	public static final int EASY_SCORE = 250;
	public static final int NORMAL_SCORE = 500;
	public static final int HARD_SCORE = 750;
	public static final int VERYHARD_SCORE = 1000;
	
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
	private MainPanel mPanel;			// メインパネル
	private GamePlayPanel playPanel;	// 実際のゲームプレイ部分のパネル
	private Box onesetBox;				//　指定される順番のワンセットボックス
	
	private Image[] tileImages;		// タイル画像の配列
	
	private int oneset;			// 指定される順番のワンセット
	private int difficulty;		// 難易度
	private int tileDrawsize;	// タイルの描画サイズ
	
	// 壁
	private int wallWidth, wallHeight;		// 壁の大きさ
	private int leftWallX, leftWallY;		// 左の壁の座標
	private int rightWallX, rightWallY;	// 右の壁の座標

	// 十字キー押下フラグ
	private boolean[] pressedCrossKey = {false, false, false, false};	
	
	private int score;		// スコア
	
	private Thread th;		// スレッド
		
	public GamePanel(MainPanel mPanel, int oneSide, int score) {
		this.mPanel = mPanel;
		this.score = score;
		
		// パネルサイズを設定
		this.setSize(mPanel.getSize());
		// レイアウトマネージャーを停止
		this.setLayout(null);
		
		// 難易度を設定
		difficulty = oneSide;
		
		// タイルパネルを生成
		playPanel = new GamePlayPanel(this, mPanel, difficulty);
		
		// タイル画像を取得する
		tileImages = playPanel.getOnesetTileImages();
		
		// ワンセットを取得する
		oneset = playPanel.getOneset();
		// タイルの描画サイズを取得する
		tileDrawsize = playPanel.getTileDrawsize();
			
		// ワンセットのボックスと順番表示ボックスを作成
		int onesetTileSize = (int)(getWidth() * 0.07);
		
		onesetBox = Box.createVerticalBox();
		for(int i = 0; i < oneset; i++) {
			try {
				JLabel onesetTileLabel = ImageLabel.getScaledImageJLabel(tileImages[i], onesetTileSize, onesetTileSize);
				onesetBox.add(onesetTileLabel, 0);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
			
		// コンポーネントサイズを設定
		playPanel.setSize(playPanel.getPreferredSize());
		onesetBox.setSize(onesetBox.getPreferredSize());
			
		// コンポーネント位置を設定
		playPanel.setLocation(horizonalCentering(this.getWidth(), playPanel.getWidth()), verticalCentering(this.getHeight(), playPanel.getHeight()));
			
		// 壁の大きさを設定
		wallWidth = tileDrawsize;
		wallHeight = getHeight();
		leftWallX = playPanel.getX() - wallWidth;
		leftWallY = 0;
		rightWallX = playPanel.getX() + playPanel.getWidth();
		rightWallY = 0;
			
		// ワンセットボックスの位置を設定
		int onesetBoxX = horizonalCentering(leftWallX, onesetBox.getWidth()) + rightWallX + wallWidth;
		int onesetBoxY = verticalCentering(this.getHeight(), onesetBox.getHeight());
		onesetBox.setLocation(onesetBoxX, onesetBoxY);
			
		// リスナーを追加
		this.addKeyListener(new GPKeyListener(this));
		// パネルが可視/不可視になると呼ばれるリスナー
		this.addAncestorListener(new GPAncestorListener(this));
			
		// コンポーネントをこのパネルに追加
		this.add(playPanel);
		this.add(onesetBox);
			
		th = new Thread(this);
		th.start();
	}
	
	@Override
	public void run() {
		while(playPanel.getPlayerRelativeLocation().y > playPanel.getTopBottomSpace() && !playPanel.isGameOver()) {
			// 十字キーが押下されていれば、その方向にプレイヤーを移動させる
			for(int i = 0; i < pressedCrossKey.length; i++) {
				if( pressedCrossKey[i] ) {
					playPanel.movePlayer(2, VECTOR[i][0], VECTOR[i][1]);
				}
			}
			
			// プレイヤーのスピードを設定
			int playerSpeed = 2;
			
			// プレイヤーが壁の外に出ないようにする
			Point relPlayerLoc = playPanel.getPlayerRelativeLocation();
			int playerWidth = playPanel.getPlayerWidth(), playerHeight = playPanel.getPlayerHeight();
			int playPanelX = playPanel.getX(), playPanelY = playPanel.getY();
			int playPanelHeight = playPanel.getHeight();
			
			if(relPlayerLoc.x + playPanelX < leftWallX + wallWidth) {
				playPanel.movePlayer(playerSpeed, RIGHT[0], RIGHT[1]);
			}
			if(relPlayerLoc.x + playPanelX + playerWidth > rightWallX) {
				playPanel.movePlayer(playerSpeed, LEFT[0], LEFT[1]);
			}
			if(playPanelY + relPlayerLoc.y + playerHeight > playPanelHeight) {
				playPanel.movePlayer(playerSpeed, UP[0], UP[1]);
			}
			
			playPanel.repaint();
			
			try {
				Thread.sleep(16);	// 16ms待機（60fps）
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		// スコアを算出
		int[][] trodTiles = playPanel.getTrodTiles();
		int trodTilesCount = 0;
		for(int i = 0; i < trodTiles.length; i++) {
			for(int j = 0; j < trodTiles[i].length; j++) {
				if(trodTiles[i][j] == 1) trodTilesCount++;
			}
		}
		for(int i = 0; i < trodTilesCount - 1; i++) {
			addScore();
		}
		System.out.println(score);
		
		// ゲームオーバーならパネルを切り替える
		if( playPanel.isGameOver() ) {
			mPanel.switchGameOverPanel(score);
		} else {
			// クリアすると次の難易度に変更
			if(difficulty == EASY) {
				mPanel.switchLevelShowPanel(LevelShowPanel.NORMAL, score);
			} else if(difficulty == NORMAL) {
				mPanel.switchLevelShowPanel(LevelShowPanel.HARD, score);
			} else if(difficulty == HARD) {
				mPanel.switchLevelShowPanel(LevelShowPanel.VERY_HARD, score);
			} else {
				System.exit(1);
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
	
	/**
	 * 難易度に応じたスコアを加算する
	 * @param difficulty	難易度
	 */
	private void addScore() {
		switch(difficulty) {
		case EASY:
			this.score += EASY_SCORE;
			break;
		case NORMAL:
			this.score += NORMAL_SCORE;
			break;
		case HARD:
			score += HARD_SCORE;
			break;
		case VERY_HARD:
			score += VERYHARD_SCORE;
			break;
		default:
			break;	
		}
	}
	
	public JPanel getPlayPanel() {
		return playPanel;
	}
}
