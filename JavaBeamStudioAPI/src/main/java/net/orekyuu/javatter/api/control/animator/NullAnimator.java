package net.orekyuu.javatter.api.control.animator;

import javafx.scene.Node;
import net.orekyuu.javatter.api.control.ControllablePane;

/**
 * アニメーションを行わずに画面遷移を行います
 *
 * @since 1.0.0
 */
public final class NullAnimator implements NodeTransitionAnimator {

    @Override
    public void transition(ControllablePane pane, Node before, Node after) {
        if (before == null) {
            pane.getChildren().add(after);
        } else {
            pane.getChildren().remove(2);
            pane.getChildren().add(2, after);
        }
    }
}
