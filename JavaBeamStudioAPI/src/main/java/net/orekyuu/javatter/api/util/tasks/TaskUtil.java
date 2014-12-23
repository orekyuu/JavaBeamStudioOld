package net.orekyuu.javatter.api.util.tasks;

import javafx.concurrent.Task;

/**
 * 非同期タスクのユーティリティクラスです。
 * @since 1.0.0
 */
public class TaskUtil {

    private TaskUtil() {

    }

    /**
     * 非同期タスクをデーモンスレッドで実行します。
     * @param task 実行するタスク
     * @since 1.0.0
     */
    public static void startTask(Task task) {
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }
}
