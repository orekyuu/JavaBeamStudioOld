package net.orekyuu.javatter.core.config;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Pane;
import net.orekyuu.javatter.api.control.ControllablePane;
import net.orekyuu.javatter.api.control.animator.FadeAnimator;
import net.orekyuu.javatter.api.util.tasks.TaskUtil;
import net.orekyuu.javatter.core.Main;
import net.orekyuu.javatter.core.plugin.loader.PluginContainer;
import net.orekyuu.javatter.core.plugin.PluginManager;

import java.net.URL;
import java.util.List;
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
        Task task = new Task() {
            @Override
            protected Object call() {
                TreeItem<String> root = new TreeItem<>();
                root.getChildren().add(new TreeItem<>("基本設定"));
                root.getChildren().add(new TreeItem<>("通知設定"));
                root.getChildren().add(new TreeItem<>("アカウント"));
                TreeItem<String> plugin = new TreeItem<>("プラグイン");
                List<PluginContainer> containerList = PluginManager.getInstance().getPluginContainers();
                containerList.stream()
                        .map(PluginContainer::getName)
                        .map(TreeItem::new)
                        .forEach(plugin.getChildren()::add);
                root.getChildren().add(plugin);
                tree.setShowRoot(false);
                Platform.runLater(() -> tree.setRoot(root));

                controllablePane.loadNode("基本設定", Main.class.getResourceAsStream("config_general.fxml"));
                controllablePane.loadNode("通知設定", Main.class.getResourceAsStream("NotificationConfig.fxml"));
                controllablePane.loadNode("アカウント", Main.class.getResourceAsStream("Account.fxml"));
                controllablePane.loadNode("プラグイン", Main.class.getResourceAsStream("plugin_tree.fxml"));
                containerList.stream().forEach(c -> controllablePane.loadNode(c.getName(),
                        ClassLoader.getSystemResourceAsStream(c.getConfigResource().orElse("net/orekyuu/javatter/core/plugin_default.fxml"))));
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
        TaskUtil.startTask(task);
        indicatorPane.setVisible(false);
    }
}