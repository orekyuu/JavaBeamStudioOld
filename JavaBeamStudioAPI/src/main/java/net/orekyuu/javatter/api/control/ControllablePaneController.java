package net.orekyuu.javatter.api.control;

import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Region;

/**
 * ScreenControllerに設定するViewのコントローラー
 *
 * @since 1.0.0
 */
public interface ControllablePaneController {
    /**
     * FXMLロード時に呼び出されます。
     *
     * @param screenController Screenを操作するためのController
     * @since 1.0.0
     */
    void setNodeParent(ControllablePane screenController);

    /**
     * インジケーター表示用のNodeが初期化時に渡される
     *
     * @param indicator indicator
     * @param region    region
     * @since 1.0.0
     */
    void setProgressNode(ProgressIndicator indicator, Region region);

    /**
     * ロードされた時に一度だけ呼び出される
     *
     * @since 1.0.0
     */
    void setup();
}
