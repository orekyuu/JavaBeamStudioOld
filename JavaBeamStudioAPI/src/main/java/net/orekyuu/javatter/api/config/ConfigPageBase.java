package net.orekyuu.javatter.api.config;

import javafx.concurrent.Task;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Region;
import net.orekyuu.javatter.api.control.ControllablePane;
import net.orekyuu.javatter.api.control.ControllablePaneController;
import net.orekyuu.javatter.api.util.tasks.TaskUtil;

/**
 * コンフィグ画面のベース
 */
public abstract class ConfigPageBase implements ControllablePaneController {

    private ProgressIndicator indicator;
    private Region veil;

    public final void setProgressNode(ProgressIndicator indicator, Region region) {
        this.indicator = indicator;
        this.veil = region;
    }

    /**
     * 非同期タスクをバインドして、インジケーターを表示します。
     *
     * @param task 非同期タスク
     */
    protected final void bindTask(Task task) {
        indicator.visibleProperty().bind(task.runningProperty());
        veil.visibleProperty().bind(task.runningProperty());
    }

    @Override
    public final void setup() {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                initializeBackground();
                return null;
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                initializeUI();
            }
        };
        bindTask(task);
        TaskUtil.startTask(task);
    }

    /**
     * バックグラウンドで行うべき処理をしてください。
     */
    protected void initializeBackground() {

    }

    /**
     * UIスレッドで行うべき処理をしてください。
     */
    protected void initializeUI() {

    }

    @Override
    public void setNodeParent(ControllablePane screenController) {
    }
}
