package net.orekyuu.javatter.core.column;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import net.orekyuu.javatter.api.API;
import net.orekyuu.javatter.api.cache.IconCache;
import net.orekyuu.javatter.api.loader.FxLoader;
import net.orekyuu.javatter.api.models.User;
import net.orekyuu.javatter.core.models.UserModel;
import net.orekyuu.javatter.api.twitter.ClientUser;
import net.orekyuu.javatter.api.util.tasks.TaskUtil;
import net.orekyuu.javatter.core.Main;
import net.orekyuu.javatter.core.config.GeneralConfigHelper;
import net.orekyuu.javatter.core.config.NameDisplayType;
import net.orekyuu.javatter.core.userprofile.UserProfilePresenter;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UserCellController implements Initializable {

    @FXML
    private Label name;
    @FXML
    private Text description;
    @FXML
    private ImageView profileimage;
    @FXML
    private AnchorPane root;

    private ClientUser clientUser;

    private User user;

    private static NameDisplayType nameDisplayType;
    private static final double DESCRIPTION_LEFT_PADDING = 60.0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (nameDisplayType == null) {
            String type = GeneralConfigHelper.loadConfigFromDB()
                    .getNameDisplayType();
            nameDisplayType = NameDisplayType.valueOf(type);
        }
        root.layoutXProperty().addListener(e -> root.setLayoutX(0));
        root.layoutYProperty().addListener(e -> root.setLayoutY(0));
    }

    @FXML
    private void openUserProfile() {
        FxLoader loader = new FxLoader();
        try {
            Parent root = loader.load(Main.class
                    .getResourceAsStream("userprofile.fxml"));
            UserProfilePresenter presenter = loader.getController();
            presenter.setUser(user);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(user.getName() + "さんのプロファイル");
            stage.initOwner(API.getInstance().getApplication().getPrimaryStage().getScene().getWindow());
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * clientUserをセットする
     *
     * @param clientUser カラムの持ち主
     */
    public void setClientUser(ClientUser clientUser) {
        this.clientUser = clientUser;
    }

    /**
     * フォーマットに合わせた名前表示
     *
     * @param user ユーザー
     * @return フォーマットに合った表示
     */
    private String getConfigFormatName(User user) {
        switch (nameDisplayType) {
            case NAME:
                return user.getName();
            case ID:
                return "@" + user.getScreenName();
            case ID_NAME:
                return "@" + user.getScreenName() + " / " + user.getName();
            case NAME_ID:
                return user.getName() + " / " + "@" + user.getScreenName();
            default:
                throw new IllegalArgumentException(nameDisplayType.name());
        }
    }

    /**
     * ユーザー内容に合わせて内容を設定
     *
     * @param user ユーザーモデル
     */
    public void updateUserCell(User user) {
        this.user = user;
        name.setText(getConfigFormatName(this.user));
        description.setText(user.getDescription());
        description.wrappingWidthProperty().bind(root.widthProperty().subtract(DESCRIPTION_LEFT_PADDING));

        Task<Image> imageTask = new Task<Image>() {
            @Override
            protected Image call() throws Exception {
                return IconCache.getImage(user.getProfileImageURL());
            }

            @Override
            protected void succeeded() {
                profileimage.setImage(getValue());
            }
        };
        TaskUtil.startTask(imageTask);
    }
}
