package net.orekyuu.javatter.api.control;

import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Region;

/**
 * ScreenControllerに設定するViewのコントローラー
 */
public interface ControllablePaneController {
    /**
     * FXMLロード時に呼び出されます。
     * @param screenController Screenを操作するためのController
     */
    void setNodeParent(ControllablePane screenController);

    void setProgressNode(ProgressIndicator indicator, Region region);

    void setup();
}
