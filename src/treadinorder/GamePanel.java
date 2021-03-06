package treadinorder;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.io.File;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.JLabel;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import treadinorder.eventlistener.GPAncestorListener;
import treadinorder.eventlistener.GPKeyListener;
import treadinorder.gamepanel.GamePlayPanel;

import static treadinorder.TOUtils.horizonalCentering;
import static treadinorder.TOUtils.verticalCentering;

public class GamePanel extends JFXPanel implements Runnable {
	// クラス変数
	// ダミーパネルを踏むサウンドの相対パス
	public static final String DUMMYPANEL_SOUND = TreadinOrder.ASSETS_PATH + "sounds/switchon.mp3";
	
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
	
	// スピード
	public static final int EM_SPEED = 3;
	public static final int HV_SPEED = 1;
	
	private MainPanel mPanel;			// メインパネル
	private GamePlayPanel playPanel;	// 実際のゲームプレイ部分のパネル
	private Box onesetBox;				//　指定される順番のワンセットボックス
	private JLabel leadNextTileLabel;	// 次の踏むタイルを指示する矢印ラベル
	
	private Image[] tileImages;		// タイル画像の配列
	
	private int oneset;			// 指定される順番のワンセット
	private int difficulty;		// 難易度
	private int tileDrawsize;	// タイルの描画サイズ
	
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
		setSize(mPanel.getSize());
		// レイアウトマネージャーを停止
		setLayout(null);
		
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
		
		// 指示矢印ラベルを設定
		leadNextTileLabel = new JLabel("→");
		leadNextTileLabel.setFont(MainPanel.DEFAULT_FONT.deriveFont(onesetTileSize));
		
		// コンポーネントサイズを設定
		playPanel.setSize(playPanel.getPreferredSize());
		onesetBox.setSize(onesetBox.getPreferredSize());
		leadNextTileLabel.setSize(onesetTileSize - 40, onesetTileSize);
		
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
		addKeyListener(new GPKeyListener(this));
		addAncestorListener(new GPAncestorListener(this));
			
		// コンポーネントをこのパネルに追加
		add(playPanel);
		add(onesetBox);
		add(leadNextTileLabel);
		
		// スレッドを開始
		th = new Thread(this);
		th.start();
	}
	
	@Override
	public void run() {
		while(playPanel.getPlayerRelativeLocation().y > playPanel.getTopBottomSpace() && !playPanel.isGameOver()) {
			// 十字キーが押下されていれば、その方向にプレイヤーを移動させる
			for(int i = 0; i < pressedCrossKey.length; i++) {
				if( pressedCrossKey[i] ) {
					if(difficulty == EASY || difficulty == NORMAL) {
						playPanel.movePlayer(EM_SPEED, VECTOR[i][0], VECTOR[i][1]);
					} else {
						playPanel.movePlayer(HV_SPEED, VECTOR[i][0], VECTOR[i][1]);
					}
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
			
			// 次に踏むタイル番号から指示矢印の位置を設定する
			int leadNextTileLabelY = onesetBox.getY() + onesetBox.getHeight() - leadNextTileLabel.getHeight() * (playPanel.getNextTreadTileNum() + 1);
			leadNextTileLabel.setLocation(onesetBox.getX() - leadNextTileLabel.getWidth(), leadNextTileLabelY);
			
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
		
		// ゲームオーバーならパネルを切り替える
		if( playPanel.isGameOver() ) {
			// スイッチ音を再生
			Media media = new Media(new File(DUMMYPANEL_SOUND).toURI().toString());
			MediaPlayer player = new MediaPlayer(media);
			player.play();
			
			try {
				Thread.sleep(500L);		// 500ms待機
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			mPanel.switchGameOverPanel(score);
		} else {
			// クリアすると次の難易度に変更
			if(difficulty == EASY) {
				mPanel.switchClearedPanel(LevelShowPanel.NORMAL, score);
			} else if(difficulty == NORMAL) {
				mPanel.switchClearedPanel(LevelShowPanel.HARD, score);
			} else if(difficulty == HARD) {
				mPanel.switchClearedPanel(LevelShowPanel.VERY_HARD, score);
			} else {
				mPanel.switchAllClearedPanel(score);
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
	
	/**
	 * そのキーの押下フラグを立てる
	 * キーコードはこのクラスのクラス変数より選択する
	 * @param key	キー
	 */
	public void setPressedKey(int key) {
		pressedCrossKey[key] = true;
	}
	
	/**
	 * そのキーの押下フラグを倒す
	 * キーコードはこのクラスのクラス変数より選択する
	 * @param key	キー
	 */
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
	
	/**
	 * ゲームプレイパネルを返す
	 * @return ゲームプレイパネル
	 */
	public GamePlayPanel getPlayPanel() {
		return playPanel;
	}
}
