package net.orekyuu.javatter.core.config;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Pane;
import net.orekyuu.javatter.core.Main;
import net.orekyuu.javatter.api.control.ControllablePane;
import net.orekyuu.javatter.api.control.animator.FadeAnimator;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * 設定ウィンドウのイベントを受け取るPresenter
 */
public class ConfigPresenter implements Initializable {

    @FXML
    private Pane indicatorPane;
    @FXML
    private TreeView<String> tree;
    @FXML
    private ControllablePane controllablePane;
    @FXML
    private TextField searchField;

    public static final String db_name = "config.db";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Service configLoadService = new Service() {
            @Override
            protected Task createTask() {
                return new Task() {
                    @Override
                    protected Object call() {
                        TreeItem<String> root = new TreeItem<>();
                        root.getChildren().add(new TreeItem<>("基本設定"));
                        root.getChildren().add(new TreeItem<>("アカウント"));
                        tree.setShowRoot(false);
                        Platform.runLater(() -> tree.setRoot(root));

                        controllablePane.loadNode("基本設定", Main.class.getResourceAsStream("config_general.fxml"));
                        controllablePane.loadNode("アカウント", Main.class.getResourceAsStream("Account.fxml"));
                        controllablePane.setAnimator(new FadeAnimator(240));
                        Platform.runLater(() -> {
                            tree.getSelectionModel().selectFirst();
                            tree.requestFocus();
                        });

                        tree.getSelectionModel().selectedItemProperty().addListener(e -> {
                            String name = tree.getSelectionModel().getSelectedItem().getValue();
                            controllablePane.setNode(name);
                        });
                        return null;
                    }
                };
            }
        };
        configLoadService.start();
        configLoadService.setOnSucceeded(e -> indicatorPane.setVisible(false));
    }
}
