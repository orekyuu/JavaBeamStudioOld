package net.orekyuu.javatter.api.control;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
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

    public void setAnimator(NodeTransitionAnimator animator) {
        this.animator = animator;
    }

    //FXMLをロード
    public void loadNode(String id, InputStream resource) {
        try {
            FXMLLoader loader = new FXMLLoader();
            Parent loadNode = loader.load(resource);
            Object obj = loader.getController();
            if (obj instanceof ControllablePaneController) {
                ControllablePaneController screen = (ControllablePaneController) obj;
                screen.setNodeParent(this);
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

        animatePane(getChildren().isEmpty() ? null : getChildren().get(0), nodes.get(id));
    }

    //アニメーションさせる
    private void animatePane(Node before, Node after) {
        animator.transition(this, before, after);
    }
}
