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
            Object obj = loader.getController();
            if (obj instanceof ControlledScreen) {
                ControlledScreen screen = (ControlledScreen) obj;
                screen.setScreenParent(this);
            }
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
            DoubleProperty xProperty = translateXProperty();
            if (!getChildren().isEmpty()) {
                Timeline fade = new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(xProperty, 0.0)), new KeyFrame(new Duration(130), e -> {
                            getChildren().remove(0);
                            getChildren().add(0, screens.get(name));
                            Timeline fadeIn = new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(xProperty, widthProperty().get() * -1)), new KeyFrame(new Duration(130), new KeyValue(xProperty, 0.0))
                            );
                            fadeIn.play();
                }, new KeyValue(xProperty, widthProperty().get()))
                );
                fade.play();
            } else {
                setLayoutX(widthProperty().get() * -1);
                getChildren().add(screens.get(name));
                Timeline fadeIn = new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(xProperty, widthProperty().get() * -1)), new KeyFrame(new Duration(130), new KeyValue(xProperty, 0.0))
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
