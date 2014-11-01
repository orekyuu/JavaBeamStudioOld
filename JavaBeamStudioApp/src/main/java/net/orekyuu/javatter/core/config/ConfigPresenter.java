package net.orekyuu.javatter.core.config;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import net.orekyuu.javatter.core.Main;
import net.orekyuu.javatter.core.control.ControllablePane;
import net.orekyuu.javatter.core.control.animator.ScrollAnimator;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * 設定ウィンドウのイベントを受け取るPresenter
 */
public class ConfigPresenter implements Initializable {

    @FXML
    private TreeView<String> tree;
    @FXML
    private ControllablePane controllablePane;
    @FXML
    private TextField searchField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TreeItem<String> root = new TreeItem<>();
        root.getChildren().add(new TreeItem<>("基本設定"));
        root.getChildren().add(new TreeItem<>("アカウント"));
        tree.setRoot(root);
        tree.setShowRoot(false);

        controllablePane.loadNode("基本設定", Main.class.getResourceAsStream("config_general.fxml"));
        controllablePane.loadNode("アカウント", Main.class.getResourceAsStream("Account.fxml"));
        controllablePane.setAnimator(new ScrollAnimator(140));
        controllablePane.setNode("基本設定");

        tree.getSelectionModel().selectedItemProperty().addListener(e -> {
            String name = tree.getSelectionModel().getSelectedItem().getValue();
            controllablePane.setNode(name);
        });
    }
}
