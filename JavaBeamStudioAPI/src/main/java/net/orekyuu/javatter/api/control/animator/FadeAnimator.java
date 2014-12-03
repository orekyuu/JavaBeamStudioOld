package net.orekyuu.javatter.api.control.animator;

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.util.Duration;
import net.orekyuu.javatter.api.control.ControllablePane;

/**
 * フェードして画面遷移します。
 */
public class FadeAnimator implements NodeTransitionAnimator {

    private int duration;

    /**
     * @param duration フェードにかかる時間(ms)
     */
    public FadeAnimator(int duration) {
        this.duration = duration;
    }

    @Override
    public void transition(ControllablePane pane, Node before, Node after) {
        if (before != null) {
            after.setOpacity(0.0);//一瞬1.0のまま表示されるのを防ぐ
            FadeTransition fadeOut = new FadeTransition(new Duration(duration), before);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(e -> {
                pane.getChildren().remove(2);
                pane.getChildren().add(2, after);
                FadeTransition fadeIn = new FadeTransition(new Duration(duration), after);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();
            });
            fadeOut.play();
        } else {
            after.setOpacity(0.0);//一瞬1.0のまま表示されるのを防ぐ
            pane.getChildren().add(2, after);
            FadeTransition fadeIn = new FadeTransition(new Duration(duration), after);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        }
    }
}
