package net.orekyuu.javatter.core.control;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import net.orekyuu.javatter.core.Main;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.HashMap;
import java.util.Map;

/**
 * ページ遷移機能のあるNode
 */
public class ScreenController extends StackPane {
    private Map<String, Node> screens = new HashMap<>();

    public void addScreen(String name, Node node) {
        screens.put(name, node);
    }

    /**
     * nameに割り当てられているNodeを返します。
     * @param name name
     * @return nameに割り当てられているNode
     */
    public Node getNode(String name) {
        return screens.get(name);
    }

    /**
     * Nodeを読み込みます。
     * @param name name
     * @param resource fxmlのパス
     */
    public void loadScreen(String name, String resource) {
        try {
            FXMLLoader loader = new FXMLLoader();
            Parent loadScreen = loader.load(Main.class.getResourceAsStream(resource));
            ControlledScreen screen = loader.getController();
            screen.setScreenParent(this);
            addScreen(name, loadScreen);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * 表示するノードを変更します。
     * @param name name
     */
    public void setScreen(String name) {
        if (screens.get(name) != null) {
            DoubleProperty opacity = opacityProperty();
            if (!getChildren().isEmpty()) {
                Timeline fade = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(opacity, 1.0)),
                        new KeyFrame(new Duration(500), e -> {
                            getChildren().remove(0);
                            getChildren().add(0, screens.get(name));
                            Timeline fadeIn = new Timeline(
                                    new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                                    new KeyFrame(new Duration(250), new KeyValue(opacity, 1.0))
                            );
                            fadeIn.play();
                        }, new KeyValue(opacity, 0.0))
                );
                fade.play();
            } else {
                setOpacity(0.0);
                getChildren().add(screens.get(name));
                Timeline fadeIn = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                        new KeyFrame(new Duration(500), new KeyValue(opacity, 1.0))
                );
                fadeIn.play();
            }
        } else {
            throw new AssertionError("screen hasn't been loaded");
        }
    }

    /**
     * Nodeを削除します。
     * @param name name
     */
    public void unloadScreen(String name) {
        screens.remove(name);
    }
}
