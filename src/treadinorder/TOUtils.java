package treadinorder;

public class TOUtils {
	/**
	 * 親に対して中央揃えにするためのX座標を返す
	 * @param x 中央揃えにしたいオブジェクトの横幅
	 * @return 中央揃えのためのX座標
	 */
	public static int horizonalCentering(int parent, int width) {
		return (parent - width) / 2;
	}
	
	/**
	 * 親に対して縦方向の中央揃えにするためのY座標を返す
	 * @param y 縦中央揃えにしたいオブジェクトの縦幅
	 * @return 縦中央揃えのためのY座標
	 */
	public static int verticalCentering(int parent, int height) {
		return (parent - height) / 2;
	}
}
