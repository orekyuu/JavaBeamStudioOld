package net.orekyuu.javatter.api.userprofile;

import javafx.concurrent.Task;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Region;
import net.orekyuu.javatter.api.models.UserModel;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * ユーザープロファイルに表示するタブのベースクラス
 * @since 1.0.0
 */
public abstract class UserProfileTabBase implements Initializable {
    private ProgressIndicator indicator;
    private Region veil;

    /**
     * ページの初期化を行います。
     * @param indicator 待機中に表示するインジケーター
     * @param region 待機中に表示するRegion
     * @since 1.0.0
     */
    public final void initialize(ProgressIndicator indicator, Region region) {
        this.indicator = indicator;
        this.veil = region;
    }

    /**
     * 非同期タスクをバインドして、インジケーターを表示します。
     *
     * @param task 非同期タスク
     * @since 1.0.0
     */
    protected final void bindTask(Task task) {
        indicator.visibleProperty().bind(task.runningProperty());
        veil.visibleProperty().bind(task.runningProperty());
    }

    /**
     * 非同期タスクをバインドし、実行します。
     * @param task 実行したい非同期タスク
     * @since 1.0.0
     */
    protected final void runTask(Task task) {
        bindTask(task);
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * 情報を表示するユーザーを設定します
     * @param user 表示するユーザー
     * @since 1.0.0
     */
    public final void setUser(UserModel user) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                initializeBackground(user);
                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                initializeUI(user);
            }
        };
        runTask(task);
    }

    /**
     * バックグラウンドで行うべき処理をしてください。
     *
     * @param user 情報を表示するユーザー
     * @since 1.0.0
     */
    protected abstract void initializeBackground(UserModel user);

    /**
     * UIスレッドで行うべき処理をしてください。
     *
     * @param user 情報を表示するユーザー
     * @since 1.0.0
     */
    protected abstract void initializeUI(UserModel user);

    /**
     * タブの名前を返す
     * @return タブの名前
     * @since 1.0.0
     */
    public abstract String getTabName();

    @Override
    public final void initialize(URL location, ResourceBundle resources) {
    }
}
