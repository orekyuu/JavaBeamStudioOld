package net.orekyuu.javatter.api;

import javafx.stage.Stage;

/**
 * アプリケーションを表すインターフェースです。
 * @since 1.0.0
 */
public interface Application {
    /**
     * アプリケーション開始時に呼び出されます。<br>
     * このメソッドはJavaFXアプリケーションスレッドで呼び出されます。
     *
     * @param args 実行時引数
     * @since 1.0.0
     */
    void onStart(String[] args);

    /**
     * ロード処理を行います。<br>
     * このメソッドはonStartのあとに呼び出されてバックグラウンドスレッドで動作します。
     * @since 1.0.0
     */
    void onLoad();

    /**
     * onLoad()の処理が終わった後に呼び出されて、JavaFXアプリケーションスレッドで動作します。
     *
     * @param primaryStage メインウィンドウが使用するStage
     * @since 1.0.0
     */
    void onCreate(Stage primaryStage);

    /**
     * アプリケーションの再起動を行います。
     * @since 1.0.0
     */
    void restart();

    /**
     * CurrentWindowを返します。
     * @return {@link CurrentWindow}
     * @since 1.0.0
     */
    CurrentWindow getCurrentWindow();

    /**
     * アプリケーションのStageを返します。
     * @return アプリケーションのStage
     * @since 1.0.0
     */
    Stage getPrimaryStage();

}
