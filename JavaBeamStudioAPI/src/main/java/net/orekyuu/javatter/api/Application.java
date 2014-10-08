package net.orekyuu.javatter.api;

import javafx.stage.Stage;

/**
 * 
 * アプリケーションを表すインターフェース
 *
 */
public interface Application {
	/**
	 * アプリケーション開始時に呼び出されます。<br>
	 * このメソッドはJavaFXアプリケーションスレッドで呼び出されます。
	 * 
	 * @param args
	 *            実行時引数
	 */
	void onStart(String[] args);
	/**
	 * ロード処理を行います。<br>
	 * このメソッドはonStartのあとに呼び出されてバックグラウンドスレッドで動作します。
	 */
	void onLoad();
	/**
	 * onLoad()の処理が終わった後に呼び出されて、JavaFXアプリケーションスレッドで動作します。
	 * @param primaryStage primaryStage
	 */
	void onCreate(Stage primaryStage);

}
