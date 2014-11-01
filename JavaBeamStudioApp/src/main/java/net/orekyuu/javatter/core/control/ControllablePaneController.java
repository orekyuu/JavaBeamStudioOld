package net.orekyuu.javatter.core.control;

/**
 * ScreenControllerに設定するViewのコントローラー
 */
public interface ControllablePaneController {
    /**
     * FXMLロード時に呼び出されます。
     * @param screenController Screenを操作するためのController
     */
    void setNodeParent(ControllablePane screenController);
}
