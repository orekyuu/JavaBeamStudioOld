package net.orekyuu.javatter.api.control;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
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
 * @since 1.0.0
 */
public class ControllablePane extends StackPane {
    private Map<String, Node> nodes = new HashMap<>();
    private NodeTransitionAnimator animator = new NullAnimator();

    private Region region = new Region();
    private ProgressIndicator indicator = new ProgressIndicator();

    /**
     * ページ遷移機能のあるNodeを作成します。
     * @since 1.0.0
     */
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

    /**
     * 遷移時のアニメーションを設定します。
     * @param animator 遷移時のアニメーション
     * @since 1.0.0
     */
    public void setAnimator(NodeTransitionAnimator animator) {
        this.animator = animator;
    }

    /**
     * FXMLのロードを行います、
     * @param id ページの識別ID
     * @param resource ページのFXML
     * @exception java.io.UncheckedIOException FXMLのロードに失敗した時
     * @since 1.0.0
     */
    public void loadNode(String id, InputStream resource) {
        try {
            FXMLLoader loader = new FXMLLoader();
            Parent loadNode = loader.load(resource);
            Object obj = loader.getController();
            if (obj instanceof ControllablePaneController) {
                ControllablePaneController screen = (ControllablePaneController) obj;
                screen.setNodeParent(this);
                screen.setProgressNode(indicator, region);
                screen.setup();
            }
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setContent(loadNode);
            nodes.put(id, scrollPane);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * 識別IDに対応するノードを破棄します。
     * @param id Nodeの識別ID
     * @since 1.0.0
     */
    public void unloadNode(String id) {
        nodes.remove(id);
    }

    /**
     * IDに対応するページへ遷移します。
     * @param id 移動するページの識別ID
     * @since 1.0.0
     * @exception java.lang.NullPointerException idに対応するノードが見つからなかった時
     */
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
