package net.orekyuu.javatter.core.column;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import net.orekyuu.javatter.api.cache.IconCache;
import net.orekyuu.javatter.api.models.StatusModel;
import net.orekyuu.javatter.api.models.UserModel;
import net.orekyuu.javatter.api.twitter.ClientUser;
import net.orekyuu.javatter.api.util.tasks.TaskUtil;
import net.orekyuu.javatter.core.config.GeneralConfigHelper;
import net.orekyuu.javatter.core.config.NameDisplayType;

public class UserCellController implements Initializable {

    @FXML
    private Label screenname;
    @FXML
    private Label name;
    @FXML
    private ImageView profileimage;
    @FXML
    private AnchorPane root;
    private ClientUser clientUser;

    /**
     * timeラベル用の時刻フォーマット
     */
    private StatusModel status;

    private static NameDisplayType nameDisplayType;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (nameDisplayType == null) {
            String type = GeneralConfigHelper.loadConfigFromDB().getNameDisplayType();
            nameDisplayType = NameDisplayType.valueOf(type);
        }
       root.layoutXProperty().addListener(e -> root.setLayoutX(0));
       root.layoutYProperty().addListener(e -> root.setLayoutY(0));
    }

    public void openUserProfile() {
    }

    /**
     * clientUserをセットする
     *
     * @param clientUser
     *            カラムの持ち主
     */
    public void setClientUser(ClientUser clientUser) {
        this.clientUser = clientUser;
    }

    /**
     * ユーザー内容に合わせて内容を設定
     *
     * @param user ユーザーモデル
     */
	public void updateUserCell(UserModel user){
		name.setText(user.getName());
        screenname.setText(user.getScreenName());
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
