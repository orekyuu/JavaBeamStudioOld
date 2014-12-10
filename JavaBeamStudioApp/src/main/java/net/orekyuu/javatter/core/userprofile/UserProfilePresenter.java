package net.orekyuu.javatter.core.userprofile;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import net.orekyuu.javatter.api.models.UserModel;
import net.orekyuu.javatter.api.userprofile.UserProfileTabBase;
import net.orekyuu.javatter.core.Main;
import net.orekyuu.javatter.api.cache.IconCache;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;

public class UserProfilePresenter implements Initializable {
    @FXML
    private ImageView icon;
    @FXML
    private Text screenName;
    @FXML
    private Text name;
    @FXML
    private TabPane tabpane;

    private UserModel user;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void setUser(UserModel userModel) {
        this.user = userModel;
        Task<Image> task = new Task<Image>() {
            @Override
            protected Image call() throws Exception {
                return IconCache.getImage(userModel.getProfileImageURL());
            }

            @Override
            protected void succeeded() {
                super.succeeded();
                icon.setImage(getValue());
                screenName.setText(userModel.getScreenName());
                name.setText(userModel.getName());
            }
        };
        Executors.newSingleThreadExecutor().submit(task);

        Arrays.asList("profiletab.fxml", "tweet_tab.fxml", "favorites_tab.fxml").stream().map(this::createTab).forEach(tabpane.getTabs()::add);

    }

    private Tab createTab(String path) {
        InputStream stream = Main.class.getResourceAsStream(path);
        Tab tab = new Tab();
        StackPane stackPane = new StackPane();
        ProgressIndicator indicator = new ProgressIndicator();
        Region region = new Region();
        region.getStyleClass().add("indicator");
        indicator.getStyleClass().add("progress-indicator");

        tab.setContent(stackPane);
        FXMLLoader loader = new FXMLLoader();
        try {
            Parent root = loader.load(stream);
            UserProfileTabBase presenter = loader.getController();
            presenter.initialize(indicator, region);

            presenter.setUser(user);
            stackPane.getChildren().add(root);
            tab.setText(presenter.getTabName());
        } catch (IOException e) {
            e.printStackTrace();
        }

        stackPane.getChildren().add(region);
        stackPane.getChildren().add(indicator);
        return tab;
    }
}
