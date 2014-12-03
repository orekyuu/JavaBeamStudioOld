package net.orekyuu.javatter.api.control;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import net.orekyuu.javatter.api.control.animator.NodeTransitionAnimator;
import net.orekyuu.javatter.api.control.animator.NullAnimator;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;

/**
 * ページ遷移機能のあるNode
 */
public class ControllablePane extends StackPane {
    private Map<String, Node> nodes = new HashMap<>();
    private NodeTransitionAnimator animator = new NullAnimator();

    private Region region = new Region();
    private ProgressIndicator indicator = new ProgressIndicator();

    public ControllablePane() {
        region.getStyleClass().add("indicator");
        indicator.getStyleClass().add("progress-indicator");
        region.setVisible(false);
        indicator.setVisible(false);
        indicator.setScaleX(0.2);
        indicator.setScaleY(0.2);
        getChildren().add(indicator);
        getChildren().add(region);
    }

    public void setAnimator(NodeTransitionAnimator animator) {
        this.animator = animator;
    }

    //FXMLをロード
    public void loadNode(String id, InputStream resource) {
        try {
            System.out.println("loadNode");
            FXMLLoader loader = new FXMLLoader();
            Parent loadNode = loader.load(resource);
            Object obj = loader.getController();
            if (obj instanceof ControllablePaneController) {
                ControllablePaneController screen = (ControllablePaneController) obj;
                screen.setNodeParent(this);
                screen.setProgressNode(indicator, region);
                screen.setup();
            }
            nodes.put(id, loadNode);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    //FXMLをアンロード
    public void unloadNode(String id) {
        nodes.remove(id);
    }

    //画面遷移
    public void setNode(String id) {
        if (nodes.get(id) == null) {
            throw new NullPointerException(id + " is not registered.");
        }

        animatePane(getChildren().size() == 2 ? null : getChildren().get(2), nodes.get(id));
    }

    //アニメーションさせる
    private void animatePane(Node before, Node after) {
        animator.transition(this, before, after);
    }
}
