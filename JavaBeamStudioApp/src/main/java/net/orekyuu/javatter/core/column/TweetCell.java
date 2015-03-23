package net.orekyuu.javatter.core.column;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import net.orekyuu.javatter.api.loader.FxLoader;
import net.orekyuu.javatter.api.models.StatusModel;
import net.orekyuu.javatter.api.twitter.ClientUser;
import net.orekyuu.javatter.core.Main;

import java.io.IOException;

public class TweetCell extends ListCell<StatusModel> {
    /**
     * 所定コントローラ
     */
    private TweetCellController tweetCellController;
    private ClientUser clientUser;

    /**
     * コンストラクタ
     *
     * @param clientuser UserStreamの持つClientUser
     */
    public TweetCell(ClientUser clientuser) {
        super();
        clientUser = clientuser;
    }

    /**
     * アイテムの内容をStatusに従って切り替える
     *
     * @param status 受け取ったステータス
     * @param empty  空かどうか
     */
    @Override
    protected void updateItem(StatusModel status, boolean empty) {
        // スーパークラスから必要な機能を継承
        super.updateItem(status, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
            tweetCellController = null;
        } else {
            // 空でない場合は
            // 名前の取得と表示
            if (tweetCellController == null) {
                FxLoader fxmlLoader = new FxLoader();
                try {
                    AnchorPane root = fxmlLoader.load(Main.class
                            .getResourceAsStream("tweetcell.fxml"));
                    DoubleBinding binding = Bindings.add(-20.0, getListView().widthProperty());
                    root.prefWidthProperty().bind(binding);
                    root.maxWidthProperty().bind(binding);
                    setGraphic(root);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                tweetCellController = fxmlLoader.getController();
                tweetCellController.setClientUser(clientUser);
            }
            if (tweetCellController != null)
                tweetCellController.updateTweetCell(status);
        }
    }
}
