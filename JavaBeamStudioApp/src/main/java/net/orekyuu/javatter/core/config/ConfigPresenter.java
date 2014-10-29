package net.orekyuu.javatter.core.config;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import net.orekyuu.javatter.core.control.ScreenController;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * 設定ウィンドウのイベントを受け取るPresenter
 */
public class ConfigPresenter implements Initializable {

    @FXML
    private TreeView<String> tree;
    @FXML
    private ScreenController screen;
    @FXML
    private TextField searchField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TreeItem<String> root = new TreeItem<>();
        root.getChildren().add(new TreeItem<>("基本設定"));
        root.getChildren().add(new TreeItem<>("アカウント"));
        tree.setRoot(root);
        tree.setShowRoot(false);

        screen.loadScreen("基本設定", "Account.fxml");
        screen.loadScreen("アカウント", "Account.fxml");

        tree.getSelectionModel().selectedItemProperty().addListener(e -> {
            String name = tree.getSelectionModel().getSelectedItem().getValue();
            screen.setScreen(name);
        });
    }
}
