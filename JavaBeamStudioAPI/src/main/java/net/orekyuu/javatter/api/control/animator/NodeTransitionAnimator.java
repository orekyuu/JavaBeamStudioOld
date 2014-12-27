package net.orekyuu.javatter.api.control.animator;

import javafx.scene.Node;
import net.orekyuu.javatter.api.control.ControllablePane;

/**
 * 画面遷移のアニメーションを提供するインターフェイス
 *
 * @since 1.0.0
 */
public interface NodeTransitionAnimator {
    /**
     * 画面遷移のリクエスト時に呼び出されます。
     *
     * @param pane   親の{@link net.orekyuu.javatter.api.control.ControllablePane}
     * @param before 古いNode
     * @param after  新しいNode
     * @since 1.0.0
     */
    void transition(ControllablePane pane, Node before, Node after);
}
