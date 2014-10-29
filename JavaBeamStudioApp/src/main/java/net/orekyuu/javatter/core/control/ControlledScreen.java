package net.orekyuu.javatter.core.control;

/**
 * ScreenControllerに設定するViewのコントローラー
 */
public interface ControlledScreen {
    /**
     * FXMLロード時に呼び出されます。
     * @param screenController Screenを操作するためのController
     */
    void setScreenParent(ScreenController screenController);
}
