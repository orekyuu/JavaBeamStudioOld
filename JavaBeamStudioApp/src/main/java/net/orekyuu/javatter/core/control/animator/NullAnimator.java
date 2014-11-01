package net.orekyuu.javatter.core.control.animator;

import javafx.scene.Node;
import net.orekyuu.javatter.core.control.ControllablePane;

/**
 * アニメーションを行わずに画面遷移を行います
 */
public class NullAnimator implements NodeTransitionAnimator {

    @Override
    public void transition(ControllablePane pane, Node before, Node after) {
        if (before == null) {
            pane.getChildren().add(after);
        } else {
            pane.getChildren().remove(0);
            pane.getChildren().add(0, after);
        }
    }
}
