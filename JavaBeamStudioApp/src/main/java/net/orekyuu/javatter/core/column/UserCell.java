package net.orekyuu.javatter.core.column;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import net.orekyuu.javatter.api.loader.FxLoader;
import net.orekyuu.javatter.api.models.User;
import net.orekyuu.javatter.core.models.UserModel;
import net.orekyuu.javatter.api.twitter.ClientUser;
import net.orekyuu.javatter.core.Main;

import java.io.IOException;

public class UserCell extends ListCell<User> {
    /**
     * 所定コントローラ
     */
    private UserCellController userCellController;
    private ClientUser clientUser;

    /**
     * コンストラクタ
     *
     * @param clientuser UserStreamの持つClientUser
     */
    public UserCell(ClientUser clientuser) {
        super();
        clientUser = clientuser;
    }

    /**
     * アイテムの内容をUserに従って切り替える
     *
     * @param user  受け取ったステータス
     * @param empty 空かどうか
     */
    @Override
    protected void updateItem(User user, boolean empty) {
        // スーパークラスから必要な機能を継承
        super.updateItem(user, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
            userCellController = null;
        } else {
            // 空でない場合は
            // 名前の取得と表示
            if (userCellController == null) {
                FxLoader fxmlLoader = new FxLoader();
                try {
                    AnchorPane root = fxmlLoader.load(Main.class
                            .getResourceAsStream("usercell.fxml"));
                    DoubleBinding binding = Bindings.add(-20.0, getListView().widthProperty());
                    root.prefWidthProperty().bind(binding);
                    root.maxWidthProperty().bind(binding);
                    setGraphic(root);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                userCellController = fxmlLoader.getController();
                userCellController.setClientUser(clientUser);
            }
            if (userCellController != null)
                userCellController.updateUserCell(user);
        }
    }
}
