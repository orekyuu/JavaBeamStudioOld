package net.orekyuu.javatter.core.control.animator;

import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;
import net.orekyuu.javatter.core.control.ControllablePane;

/**
 * 横にスライドして画面遷移します。
 */
public class ScrollAnimator implements NodeTransitionAnimator {

    private int duration;

    /**
     * @param duration 画面遷移にかかる時間(ms)
     */
    public ScrollAnimator(int duration) {
        this.duration = duration;
    }

    @Override
    public void transition(ControllablePane pane, Node before, Node after) {
        if (before != null) {
            after.setTranslateX(pane.getWidth() * -1);
            TranslateTransition out = new TranslateTransition(new Duration(duration), before);
            out.setFromX(0);
            out.setToX(pane.getWidth());
            out.setOnFinished(e -> {
                pane.getChildren().remove(0);
                pane.getChildren().add(0, after);
                TranslateTransition in = new TranslateTransition(new Duration(duration), after);
                in.setFromX(pane.getWidth() * -1);
                in.setToX(0);
                in.play();
            });
            out.play();
        } else {
            pane.getChildren().add(after);
            TranslateTransition in = new TranslateTransition(new Duration(duration), after);
            in.setFromX(pane.getWidth() * -1);
            in.setToX(0);
            in.play();
        }
    }
}
