package treadinorder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Maze {
	// クラス変数
	public static final int DUMMY = 0;		// ダミー
	public static final int ROAD = 1;		// 道
	
	/*
	 * 上(0), 左(1), 下(2), 右(3)のベクトル
	 * ※プログラム上では下が正の方向
	 */
	public static final int[][] VECTOR = {{0, -1}, {-1, 0}, {0, 1}, {1, 0}};
	
	// インスタンス変数
	private int row, col;	// マップの行列
	private int[][] map;	// 全体のマップ
	
	private boolean isReachedTop;
	
	public Maze(int row, int col) {
		// row, colが奇数でなければ強制的に奇数に
		this.row = (row % 2 != 0) ? row : row + 1;
		this.col = (col % 2 != 0) ? col : col + 1;
		// 0で初期化の為全てダミーになる
		map = new int[this.row][this.col];
		
		// 下面から列をランダムに選び道を作り始める
		isReachedTop = false;
		makeRoad(this.row - 1, new Random().nextInt(col));
	}
	
	/**
	 * 再帰的に道を作る。上面に達すると終了する
	 * @param y 作るy座標 
	 * @param x 作るx座標
	 */
	public void makeRoad(int y, int x) {
		// 道を作る
		map[y][x] = ROAD;
		if(y == 0) {
			isReachedTop = true;
			return;
		}
		
		// ベクトルをリスト化しシャッフルする
		List<int[]> direction = Arrays.asList(VECTOR);
		Collections.shuffle(direction);
		
		// 各ベクトルについて座標から２マス先に作れるか確かめる
		// 可能ならそこにも道を作り、再帰でそこを起点に道を作る
		for(int[] eachV : direction) {
			if(isReachedTop) return;		// 上面に達していたら処理しない
			int dy = eachV[0], dx = eachV[1];
			if( isMakable(y + dy * 2, x + dx * 2) ) {
				map[y + dy][x + dx] = ROAD;
				makeRoad(y + dy * 2, x + dx * 2);
			}
		}
	}
	
	/**
	 * 指定した座標に道が作れるかどうか確認する。マップの範囲内でありかつダミーであれば作成可能。
	 * @param y 指定した行
	 * @param x 指定した列
	 * @return 作れるならtrue, 作れなければfalse
	 */
	public boolean isMakable(int y, int x) {
		return (y > -1) && (y < row) && (x > -1) && (x < col) && map[y][x] == DUMMY;
	}
	
	/**
	 * 生成された迷路のマップを取得する
	 * @return 生成されたマップ
	 */
	public int[][] getMap() {
		return map;
	}
}
