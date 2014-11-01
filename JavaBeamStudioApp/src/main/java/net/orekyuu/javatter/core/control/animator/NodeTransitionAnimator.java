package net.orekyuu.javatter.core.control.animator;

import javafx.scene.Node;
import net.orekyuu.javatter.core.control.ControllablePane;

/**
 * 画面遷移のアニメーションを提供するインターフェイス
 */
public interface NodeTransitionAnimator {
    /**
     * 画面遷移のリクエスト時に呼び出されます。
     *
     * @param pane 親のControllablePane
     * @param before 古いNode
     * @param after 新しいNode
     */
    void transition(ControllablePane pane, Node before, Node after);
}
